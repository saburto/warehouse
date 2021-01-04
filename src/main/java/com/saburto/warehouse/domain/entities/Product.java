package com.saburto.warehouse.domain.entities;

import static java.util.stream.Collectors.toList;
import java.util.List;
import java.util.Map;

public class Product {

    private final long stock;
    private final ProductDefinition definition;
    private final List<Article> articles;

    public Product(ProductDefinition definition, Map<Integer, Article> articles) {
        this.definition = definition;

        this.articles = definition.getContainigArticleIds().stream()
            .map(a -> articles.getOrDefault(a, Article.ofNoStock(a)))
            .collect(toList());

        this.stock = calculateStock();
    }

    private Product(ProductDefinition definition, List<Article> articles) {
        this.definition = definition;
        this.articles = articles;
        this.stock = calculateStock();
    }

    private long calculateStock() {
        return articles.stream()
            .mapToLong(a -> a.getStock() / definition.amountArticle(a.getId()))
            .min()
            .orElse(0);
    }

    public String getName() {
        return this.definition.getName();
    }

    public long getStock() {
        return stock;
    }

    public Product sell() {

        if (!hasStock()) {
            throw new NoStockAvailableException(getName());
        }

        var newArticles = articles.stream()
            .map(a -> a.sell(definition.amountArticle(a.getId())))
            .collect(toList());

        return new Product(definition, newArticles);
    }

    private boolean hasStock() {
        return stock > 0;
    }

    public static class NoStockAvailableException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public NoStockAvailableException(String name) {
            super("No available stock for " + name);
        }
    }
}
