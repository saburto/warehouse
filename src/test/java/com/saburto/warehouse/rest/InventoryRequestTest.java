package com.saburto.warehouse.rest;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saburto.warehouse.rest.requests.ArticleRequest;
import com.saburto.warehouse.rest.requests.InventoryRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InventoryRequestTest {

    private ObjectMapper mapper;
    private Validator validator;

    @BeforeEach
    void setup() {
        mapper = new ObjectMapper();
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }


    @Test
    void inventory_is_parsed_to_object() throws JsonMappingException, JsonProcessingException {
        String rawJson = "{\"inventory\": [{\"art_id\": 10, \"name\":\"product1\", \"stock\": 10}]}";

        var inventory = mapper.readValue(rawJson, InventoryRequest.class);

        assertThat(inventory.getInventory())
            .hasSize(1)
            .first()
            .extracting(ArticleRequest::getArtId, ArticleRequest::getName, ArticleRequest::getStock)
            .contains(10, "product1", 10);
    }


    @Test
    void validate_inventory_empty() throws JsonMappingException, JsonProcessingException {
        String rawJson = "{\"inventory\": []}";

        var inventory = mapper.readValue(rawJson, InventoryRequest.class);

        var errors = validator.validate(inventory);
        assertThat(errors).hasSize(1)
            .first()
            .extracting(ConstraintViolation::getMessage)
            .isEqualTo("Inventory must contain at least 1 article");
    }

    @Test
    void validate_inventory_null() throws JsonMappingException, JsonProcessingException {
        String rawJson = "{}";

        var inventory = mapper.readValue(rawJson, InventoryRequest.class);

        var errors = validator.validate(inventory);
        assertThat(errors).hasSize(1).first().extracting(ConstraintViolation::getMessage)
                .isEqualTo("Inventory must not be null");
    }

}
