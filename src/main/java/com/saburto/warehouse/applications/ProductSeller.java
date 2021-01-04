package com.saburto.warehouse.applications;

import com.saburto.warehouse.domain.entities.Product;
import com.saburto.warehouse.domain.services.InventoryRepository;
import com.saburto.warehouse.domain.services.ProductRepository;
import org.springframework.transaction.annotation.Transactional;

public class ProductSeller {

    private ProductRepository productRepository;
    private InventoryRepository inventoryRepository;

    public ProductSeller(ProductRepository productRepository, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public void sell(String name) {

        var definition = productRepository.getProducDefinition(name);
        var inventory = inventoryRepository.findInventoryOf(definition.getContainigArticleIds());

        var product = new Product(definition, inventory);

        product.sell();

        inventoryRepository.removeFromStock(definition.getArticlesAmount());
    }

}
