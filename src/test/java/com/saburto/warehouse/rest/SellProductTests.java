package com.saburto.warehouse.rest;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saburto.warehouse.repo.AbstractMysqlContainerBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Tag("component-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {SellProductTests.Initializer.class})
public class SellProductTests extends AbstractMysqlContainerBaseTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String baseUrl;

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(configurableApplicationContext,
                                                                      "spring.datasource.url=" + MY_SQL_CONTAINER.getJdbcUrl(),
                                                                      "spring.datasource.driver-class-name="+ MY_SQL_CONTAINER.getDriverClassName(),
                                                                      "spring.datasource.username=" + MY_SQL_CONTAINER.getUsername(),
                                                                      "spring.datasource.password=" + MY_SQL_CONTAINER.getPassword());
        }
    }

    @BeforeEach
    void setup() throws SQLException {
        baseUrl = "http://localhost:" + port;

        jdbcTemplate.execute("delete from Containing_Articles");
        jdbcTemplate.execute("delete from Product");
        jdbcTemplate.execute("delete from Article");
    }

    @Test
    void sell_a_product_not_found_returns_error() throws IOException, URISyntaxException {

        String path = String.format("/products/%s/stock", "table-a");

        var response = restTemplate.exchange(baseUrl + path, HttpMethod.DELETE, null, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);

    }

    @Test
    void sell_a_product() throws IOException {

        int initialStock = 1;

        insertProducts(initialStock);

        checkStock(initialStock);

        String path = String.format("/products/%s/stock", "table-a");

        var delResponse = restTemplate.exchange(baseUrl + path, HttpMethod.DELETE, null, String.class);

        assertThat(delResponse.getStatusCodeValue()).isEqualTo(200);

        checkStock(0);

    }

    @Test
    void sell_a_product_with_enough_stock() throws IOException {

        int initialStock = 4;

        insertProducts(initialStock);

        checkStock(initialStock);

        String path = String.format("/products/%s/stock", "table-a");

        var delResponse =
                restTemplate.exchange(baseUrl + path, HttpMethod.DELETE, null, String.class);

        assertThat(delResponse.getStatusCodeValue()).isEqualTo(200);

        checkStock(3);

    }

    @Test
    void sell_a_product_with_no_stock() throws IOException {

        int initialStock = 0;

        insertProducts(initialStock);

        checkStock(initialStock);

        String path = String.format("/products/%s/stock", "table-a");

        var delResponse =
                restTemplate.exchange(baseUrl + path, HttpMethod.DELETE, null, String.class);

        assertThat(delResponse.getStatusCodeValue()).isEqualTo(409);

        checkStock(0);

    }

    @Test
    void sell_a_product_with_limited_stock_and_multiple_threads() throws IOException, InterruptedException, ExecutionException, TimeoutException {

        int theads = 8;

        int initialStock = 2;
        insertProducts(initialStock);
        checkStock(initialStock);

        String path = String.format("/products/%s/stock", "table-a");

        var executor = Executors.newFixedThreadPool(theads);

        var countSoldProducts = new AtomicInteger();

        var futures = IntStream.range(0, theads).mapToObj( i ->

        CompletableFuture.runAsync(() -> {

                var res = restTemplate.exchange(baseUrl + path, HttpMethod.DELETE, null, String.class);
                if (res.getStatusCodeValue() == 200) {
                    countSoldProducts.incrementAndGet();
                }

            }, executor)).collect(Collectors.toList());


        var combined = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));

        combined.get(60, TimeUnit.SECONDS);


        checkStock(0);
        assertThat(countSoldProducts.intValue()).isEqualTo(initialStock);


    }

    private void checkStock(int expectedStock) throws JsonProcessingException, JsonMappingException {
        var response = restTemplate.getForEntity(baseUrl + "/catalog", String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        var jsonNode = mapper.readTree(response.getBody());

        assertThat(jsonNode.has("products")).isTrue();

        var products = jsonNode.get("products");
        assertThat(products.size()).isEqualTo(1);

        var product = products.get(0);
        assertThat(product.get("stock").asInt()).isEqualTo(expectedStock);
        assertThat(product.get("name").asText()).isEqualTo("table-a");
    }

    private void insertProducts(int initialStock) {
        jdbcTemplate.update("insert into Article values(1, 'a', ?)", initialStock * 2);
        jdbcTemplate.execute("insert into Product values('table-a')");
        jdbcTemplate.execute("insert into Containing_Articles values('table-a', 1, 2)");
    }
}
