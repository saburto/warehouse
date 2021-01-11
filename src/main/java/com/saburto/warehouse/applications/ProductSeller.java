package com.saburto.warehouse.applications;

import com.saburto.warehouse.domain.entities.Product;
import com.saburto.warehouse.domain.services.InventoryRepository;
import com.saburto.warehouse.domain.services.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductSeller {

    private ProductRepository productRepository;
    private InventoryRepository inventoryRepository;

    @Autowired
    public ProductSeller(ProductRepository productRepository, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public void sell(String name) {

        var definition = productRepository.getProducDefinition(name).orElseThrow(() -> new NoProductFoundException(name));
        var inventory = inventoryRepository.findInventoryOf(definition.getContainigArticleIds());

        var product = new Product(definition, inventory);

        product.sell();

        inventoryRepository.removeFromStock(definition.getArticlesAmount());
    }

}
