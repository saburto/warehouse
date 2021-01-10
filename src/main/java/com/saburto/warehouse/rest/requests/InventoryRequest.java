package com.saburto.warehouse.rest.requests;

import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.saburto.warehouse.domain.entities.Article;


public class InventoryRequest {

    @Size(min = 1, message = "Inventory must contain at least 1 article")
    @NotNull(message = "Inventory must not be null")
    private final List<ArticleRequest> inventory;

    @JsonCreator
    public InventoryRequest(@JsonProperty("inventory") List<ArticleRequest> inventory) {
        this.inventory = inventory;
    }

    public List<ArticleRequest> getInventory() {
        return inventory;
    }

    public List<Article> toListOfArticle() {
        return inventory.stream()
            .map(ArticleRequest::toArticle)
            .collect(toList());
    }

}
