package com.saburto.warehouse.rest.requests;

import static java.util.stream.Collectors.toMap;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.saburto.warehouse.domain.entities.ProductDefinition;

public class ProductRequest {

    @NotBlank
    private final String name;

    @NotNull
    @Size(min = 1, message = "Containing articles must contain at least 1 article")
    private final List<ContainingArticleRequest> containingArticles;

    @JsonCreator
    public ProductRequest(@JsonProperty("name") String name,
                          @JsonProperty("contain_articles") List<ContainingArticleRequest> containingArticles) {
        this.name = name;
        this.containingArticles = containingArticles;
    }

    ProductDefinition toProductDefinition() {
        var articles = containingArticles.stream()
            .collect(toMap(ContainingArticleRequest::getArticleId,
                           ContainingArticleRequest::getAmountOf));

        return new ProductDefinition(name, articles);
    }




}
