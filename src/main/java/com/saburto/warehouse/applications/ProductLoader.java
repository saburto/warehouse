package com.saburto.warehouse.applications;

import java.util.List;
import com.saburto.warehouse.domain.entities.ProductDefinition;
import com.saburto.warehouse.domain.services.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductLoader {

    private static final Logger LOG = LoggerFactory.getLogger(ProductLoader.class);

    private final ProductRepository productRepository;

    @Autowired
    public ProductLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public void load(List<ProductDefinition> products) {

        try {
            productRepository.upsertAll(products);
            LOG.info("Loaded {} products", products.size());
        } catch (DataIntegrityViolationException e) {
            LOG.error("No article found", e);
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
