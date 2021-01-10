package com.saburto.warehouse.rest;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Tag("component-test")
class LoadProductTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String productsFile;

    private String baseUrl;

    private HttpHeaders headers;

    @BeforeEach
    void setup() throws IOException, URISyntaxException {

        productsFile = Files.readString(Path.of(getClass().getResource("/products.json").toURI()));
        jdbcTemplate.execute("delete from Containing_Articles");
        jdbcTemplate.execute("delete from Product");
        jdbcTemplate.execute("delete from Article");

        baseUrl = "http://localhost:" + port;

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void load_product_file() throws IOException, URISyntaxException {

        jdbcTemplate.execute("insert into Article values(1, 'a', 2)");
        jdbcTemplate.execute("insert into Article values(2, 'b', 2)");
        jdbcTemplate.execute("insert into Article values(3, 'c', 2)");
        jdbcTemplate.execute("insert into Article values(4, 'd', 2)");


        var request = new HttpEntity<String>(productsFile, headers);

        var response = restTemplate.postForEntity(baseUrl + "/products/bulk", request, String.class);


        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void load_product_returns_error_if_article_is_not_found() throws IOException, URISyntaxException {


        var request = new HttpEntity<String>(productsFile, headers);

        var response = restTemplate.postForEntity(baseUrl + "/products/bulk", request, String.class);


        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

}
