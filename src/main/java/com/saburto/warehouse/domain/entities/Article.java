package com.saburto.warehouse.domain.entities;

public class Article {

    private final int id;
    private final String name;
    private final int stock;

    public Article(int id, String name, int stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public Article sell(int amountArticle) {
        return new Article(id, name, stock - amountArticle);
    }

    public static Article ofNoStock(int id) {
        return new Article(id, null, 0);
    }

}
