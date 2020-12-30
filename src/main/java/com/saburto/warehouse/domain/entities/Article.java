package com.saburto.warehouse.domain.entities;

public class Article {

    public static final Article ZERO_STOCK = new Article(0, null, 0);
    private final int id;
    private final String name;
    private final long stock;

    public Article(int id, String name, long stock) {
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

    public long getStock() {
        return stock;
    }

}
