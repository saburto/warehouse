package com.saburto.warehouse.rest;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
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

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Tag("component-test")
class LoadInventoryTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void load_inventory_file() throws IOException, URISyntaxException {


        String inventoryFile = Files.readString(Path.of(getClass().getResource("/inventory.json").toURI()));

        var baseUrl = "http://localhost:" + port;

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var request = new HttpEntity<String>(inventoryFile, headers);

        var response = restTemplate.postForEntity(baseUrl + "/articles/inventory/bulk", request, String.class);


        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

}
