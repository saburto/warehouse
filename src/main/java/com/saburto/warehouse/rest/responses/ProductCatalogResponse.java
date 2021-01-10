package com.saburto.warehouse.rest.responses;

import com.saburto.warehouse.domain.entities.Product;

public class ProductCatalogResponse {

    private final String name;
    private final int stock;

    public ProductCatalogResponse(Product product) {
      this.name = product.getName();
      this.stock = product.getStock();
    }

    public int getStock() {
      return stock;
    }

    public String getName() {
      return name;
    }



}
