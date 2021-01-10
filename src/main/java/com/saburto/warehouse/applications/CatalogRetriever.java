package com.saburto.warehouse.applications;

import com.saburto.warehouse.domain.entities.Catalog;
import com.saburto.warehouse.domain.services.InventoryRepository;
import com.saburto.warehouse.domain.services.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CatalogRetriever {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    @Autowired
    public CatalogRetriever(ProductRepository productRepository, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public Catalog getCatalog() {
        return new Catalog(productRepository.findProductDefinitions(),
                           inventoryRepository.findArticleInventory());
    }
}
