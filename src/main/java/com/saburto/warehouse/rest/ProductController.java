package com.saburto.warehouse.rest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.saburto.warehouse.applications.NoProductFoundException;
import com.saburto.warehouse.applications.ProductLoader;
import com.saburto.warehouse.applications.ProductSeller;
import com.saburto.warehouse.domain.entities.Product.NoStockAvailableException;
import com.saburto.warehouse.rest.requests.ProductLoadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final ProductSeller seller;

    @Autowired
    public ProductController(ProductLoader loader, ProductSeller seller) {
        this.loader = loader;
        this.seller = seller;
    }

    @PostMapping("/bulk")
    public void load(@NotNull @Valid @RequestBody ProductLoadRequest products) {
        try {
            loader.load(products.toListOfProducDefintions());
        } catch (ProductLoader.NoArticleDefinedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Article not found");
        }
    }

    @DeleteMapping("/{product_name}/stock")
    public void sellProduct(@PathVariable("product_name") String name) {
        try {
            seller.sell(name);
        } catch (NoProductFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        } catch (NoStockAvailableException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Not stock available");
        }
    }


}
