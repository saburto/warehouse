package com.saburto.warehouse.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.saburto.warehouse.domain.entities.Article;

public class ArticleRequest {

    private final int artId;
    private final String name;
    private final long stock;

    @JsonCreator
    public ArticleRequest(@JsonProperty("art_id") int artId,
                          @JsonProperty("name") String name,
                          @JsonProperty("stock") long stock) {
        this.artId = artId;
        this.name = name;
        this.stock = stock;
    }

    public int getArtId() {
        return artId;
    }

    public String getName() {
        return name;
    }

    public long getStock() {
        return stock;
    }

    Article toArticle() {
        return new Article(getArtId(), getName(), getStock());
    }

}
