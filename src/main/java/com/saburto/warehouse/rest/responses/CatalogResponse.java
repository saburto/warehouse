package com.saburto.warehouse.rest.responses;

import static java.util.stream.Collectors.toList;
import java.util.List;
import com.saburto.warehouse.domain.entities.Catalog;

public class CatalogResponse {

    private final List<ProductCatalogResponse> products;

    public CatalogResponse(Catalog catalog) {
        this.products = catalog.getProducts()
            .stream()
            .map(ProductCatalogResponse::new)
            .collect(toList());
    }

    public List<ProductCatalogResponse> getProducts() {
        return products;
    }

}
