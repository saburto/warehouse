package com.saburto.warehouse.applications;

import java.util.List;
import com.saburto.warehouse.domain.entities.ProductDefinition;
import com.saburto.warehouse.domain.services.ProductRepository;
import org.springframework.dao.DataIntegrityViolationException;

public class ProductLoader {

    private final ProductRepository productRepository;

    public ProductLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void load(List<ProductDefinition> products) {

        try {
            productRepository.upsertAll(products);
        } catch (DataIntegrityViolationException e) {
            throw new NoArticleDefinedException(e);
        }
    }

    public static class NoArticleDefinedException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public NoArticleDefinedException(DataIntegrityViolationException e) {
            super(e);
        }


    }


}
