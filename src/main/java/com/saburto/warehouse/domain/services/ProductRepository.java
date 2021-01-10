package com.saburto.warehouse.domain.services;

import java.util.List;
import java.util.Optional;
import com.saburto.warehouse.domain.entities.ProductDefinition;

public interface ProductRepository {

    void upsertAll(List<ProductDefinition> products);

    List<ProductDefinition> findProductDefinitions();

    Optional<ProductDefinition> getProducDefinition(String name);

}
