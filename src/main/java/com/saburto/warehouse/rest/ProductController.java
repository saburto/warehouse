package com.saburto.warehouse.rest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.saburto.warehouse.applications.ProductLoader;
import com.saburto.warehouse.rest.requests.ProductLoadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/products")
@Validated
public class ProductController {

    private final ProductLoader loader;

    @Autowired
    public ProductController(ProductLoader loader) {
        this.loader = loader;
    }

    @PostMapping("/bulk")
    public void load(@NotNull @Valid @RequestBody ProductLoadRequest products) {
        try {
            loader.load(products.toListOfProducDefintions());
        } catch (ProductLoader.NoArticleDefinedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Article not found");
        }
    }


}
