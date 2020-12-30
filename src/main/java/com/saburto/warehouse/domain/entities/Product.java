package com.saburto.warehouse.domain.entities;

import java.util.Map;
import java.util.function.ToLongFunction;
import com.saburto.warehouse.domain.entities.ProductDefinition.ArticleDefinition;

public class Product {

    private final String name;
    private final long stock;

    public Product(ProductDefinition definition, Map<Integer, Article> articles) {
        this.name = definition.getName();
        this.stock = calculateStock(definition, articles);
    }

    private long calculateStock(ProductDefinition definition, Map<Integer, Article> articles) {

        return definition.getContainigArticles()
            .stream()
            .mapToLong(divideStockByNecessaryAmount(articles))
            .min()
            .orElse(0);
    }

    private ToLongFunction<ArticleDefinition> divideStockByNecessaryAmount(Map<Integer, Article> articles) {
        return d -> getArticleStock(articles, d.getId()) / d.getAmount();
    }

    private long getArticleStock(Map<Integer, Article> articles, int id) {
        return articles.getOrDefault(id, Article.ZERO_STOCK).getStock();
    }

    public String getName() {
        return name;
    }

    public long getStock() {
        return stock;
    }


}
