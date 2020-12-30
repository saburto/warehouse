package com.saburto.warehouse.applications;

import com.saburto.warehouse.domain.entities.Catalog;
import com.saburto.warehouse.domain.services.InventoryRepository;
import com.saburto.warehouse.domain.services.ProductRepository;

public class CatalogRetriever {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public CatalogRetriever(ProductRepository productRepository, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public Catalog getCatalog() {
        return new Catalog(productRepository.findProductDefinitions(),
                           inventoryRepository.findArticleInventory());
    }
}
