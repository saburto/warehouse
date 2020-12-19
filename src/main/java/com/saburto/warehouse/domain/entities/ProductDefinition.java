package com.saburto.warehouse.domain.entities;

import java.util.List;

public class ProductDefinition {

    private final String name;
    private final List<ArticleDefinition> containingArticles;

    public ProductDefinition(String name, List<ArticleDefinition> containingArticles) {
        this.name = name;
        this.containingArticles = containingArticles;
    }

    public String getName() {
        return name;
    }

    public List<ArticleDefinition> getContainigArticles() {
        return containingArticles;
    }

    public static class ArticleDefinition {
        private final int id;
        private final int amount;

        public ArticleDefinition(int id, int amount) {
          this.id = id;
          this.amount = amount;
        }

        public int getId() {
          return id;
        }

        public int getAmount() {
          return amount;
        }
    }
}
