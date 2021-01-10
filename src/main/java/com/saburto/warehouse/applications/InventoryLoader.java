package com.saburto.warehouse.applications;

import java.util.List;

import com.saburto.warehouse.domain.entities.Article;
import com.saburto.warehouse.domain.services.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryLoader {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryLoader.class);

    private final InventoryRepository repo;

    @Autowired
    public InventoryLoader(InventoryRepository repo) {
        this.repo = repo;
    }

    public void load(List<Article> articles) {
        repo.upsertAll(articles);

        LOG.info("Inventory loaded with {} articles", articles.size());
    }

}
