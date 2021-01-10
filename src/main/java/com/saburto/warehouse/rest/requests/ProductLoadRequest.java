package com.saburto.warehouse.rest.requests;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.saburto.warehouse.domain.entities.ProductDefinition;

public class ProductLoadRequest {

    @Size(min = 1, message = "Products must contain at least 1 item")
    @NotNull(message = "Products must not be null")
    private final List<ProductRequest> products;

    @JsonCreator
    public ProductLoadRequest(@JsonProperty("products") List<ProductRequest> products) {
      this.products = products;
    }

    public List<ProductDefinition> toListOfProducDefintions() {
        return products.stream()
            .map(ProductRequest::toProductDefinition)
            .collect(Collectors.toList());
    }

}
