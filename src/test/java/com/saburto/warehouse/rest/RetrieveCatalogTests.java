package com.saburto.warehouse.rest;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.net.URISyntaxException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Tag("component-test")
class RetrieveCatalogTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String baseUrl;

    @BeforeEach
    void setup() {
        baseUrl = "http://localhost:" + port;

        jdbcTemplate.execute("delete from Containing_Articles");
        jdbcTemplate.execute("delete from Product");
        jdbcTemplate.execute("delete from Article");


    }

    @Test
    void get_empty_catalog() throws IOException, URISyntaxException {

        var response = restTemplate.getForEntity(baseUrl + "/catalog", String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        var jsonNode = mapper.readTree(response.getBody());

        assertThat(jsonNode.has("products")).isTrue();
        JsonNode products = jsonNode.get("products");
        assertThat(products.size()).isEqualTo(0);

    }

    @Test
    void get_catalog_with_one_product() throws IOException, URISyntaxException {

        jdbcTemplate.execute("insert into Article values(1, 'a', 2)");
        jdbcTemplate.execute("insert into Product values('table-a')");
        jdbcTemplate.execute("insert into Containing_Articles values('table-a', 1, 2)");

        var response = restTemplate.getForEntity(baseUrl + "/catalog", String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        var jsonNode = mapper.readTree(response.getBody());

        assertThat(jsonNode.has("products")).isTrue();

        var products = jsonNode.get("products");
        assertThat(products.size()).isEqualTo(1);

        var product = products.get(0);
        assertThat(product.get("stock").asInt()).isEqualTo(1);
        assertThat(product.get("name").asText()).isEqualTo("table-a");

    }

}
