package com.saburto.warehouse.rest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.saburto.warehouse.applications.InventoryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article/inventory")
@Validated
public class InventoryController {

    private final InventoryLoader loader;

    @Autowired
    public InventoryController(InventoryLoader loader) {
        this.loader = loader;
    }

    @PostMapping("/bulk")
    public void load(@NotNull @Valid @RequestBody InventoryRequest inventory) {
        loader.load(inventory.toListOfArticle());
    }
}
