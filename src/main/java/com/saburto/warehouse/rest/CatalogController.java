package com.saburto.warehouse.rest;

import com.saburto.warehouse.applications.CatalogRetriever;
import com.saburto.warehouse.rest.responses.CatalogResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

    private final CatalogRetriever catalogRetriever;

    @Autowired
    public CatalogController(CatalogRetriever catalogRetriever) {
        this.catalogRetriever = catalogRetriever;
    }

    @GetMapping
    public CatalogResponse getCatalog() {
        return new CatalogResponse(catalogRetriever.getCatalog());
    }

}
