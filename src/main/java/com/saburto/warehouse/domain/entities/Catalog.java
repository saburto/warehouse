package com.saburto.warehouse.domain.entities;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Catalog {

    private List<Product> products;
    private Map<Integer, Article> stockArticles;

    public Catalog(List<ProductDefinition> productDefinition, List<Article> articles) {
        requireNonNull(productDefinition, "Product definition must not be null");
        requireNonNull(articles, "Articles inventory must not be null");

        stockArticles = articles.stream().collect(toMap(Article::getId, Function.identity()));

        this.products = productDefinition.stream()
            .map(this::toProduct)
            .collect(toList());

    }

    private Product toProduct(ProductDefinition definition) {
        return new Product(definition, stockArticles);
    }


    public List<Product> getProducts() {
        return products;
    }

}
