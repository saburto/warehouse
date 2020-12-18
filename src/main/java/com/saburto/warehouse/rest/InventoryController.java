package com.saburto.warehouse.rest;

import com.saburto.warehouse.applications.InventoryLoader;

public class InventoryController {

    private final InventoryLoader loader;

    public InventoryController(InventoryLoader loader) {
        this.loader = loader;
    }

    public void load(InventoryRequest inventory) {
        loader.load(inventory.toListOfArticle());
    }


}
