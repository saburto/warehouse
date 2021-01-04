package com.saburto.warehouse.domain.entities;

import java.util.Map;
import java.util.Set;

public class ProductDefinition {

    private final String name;
    private final Map<Integer, Integer> containingArticles;

    public ProductDefinition(String name, Map<Integer, Integer> containingArticles) {
        this.name = name;
        this.containingArticles = containingArticles;
    }

    public String getName() {
        return name;
    }

    public int amountArticle(int id) {
        return containingArticles.getOrDefault(id, 0);
    }

    public Set<Integer> getContainigArticleIds() {
        return containingArticles.keySet();
    }

    public Map<Integer, Integer> getArticlesAmount() {
        return Map.copyOf(containingArticles);
    }
}
