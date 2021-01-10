package com.saburto.warehouse.rest.requests;

import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.saburto.warehouse.domain.entities.Article;
import org.springframework.lang.NonNull;

public class ArticleRequest {

    @NonNull
    private final Integer artId;
    @NotBlank
    private final String name;
    @NonNull
    private final Long stock;

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
