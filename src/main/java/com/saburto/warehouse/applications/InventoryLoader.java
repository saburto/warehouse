package com.saburto.warehouse.applications;

import java.util.List;

import com.saburto.warehouse.domain.entities.Article;
import com.saburto.warehouse.domain.services.InventoryRepository;

public class InventoryLoader {

    private final InventoryRepository repo;

    public InventoryLoader(InventoryRepository repo) {
        this.repo = repo;
    }

    public void load(List<Article> articles) {
        repo.upsertAll(articles);
    }

}
