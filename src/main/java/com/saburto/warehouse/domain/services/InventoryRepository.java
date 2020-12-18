package com.saburto.warehouse.domain.services;

import java.util.List;

import com.saburto.warehouse.domain.entities.Article;

public interface InventoryRepository {

    void upsertAll(List<Article> articles);

}
