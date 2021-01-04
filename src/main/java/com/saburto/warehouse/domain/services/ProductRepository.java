package com.saburto.warehouse.domain.services;

import java.util.List;
import com.saburto.warehouse.domain.entities.ProductDefinition;

public interface ProductRepository {

    void upsertAll(List<ProductDefinition> products);

    List<ProductDefinition> findProductDefinitions();

    ProductDefinition getProducDefinition(String name);

}
