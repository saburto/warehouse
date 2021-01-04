package com.saburto.warehouse.domain.services;

import java.util.List;
import java.util.Map;
import java.util.Set;
import com.saburto.warehouse.domain.entities.Article;

public interface InventoryRepository {

    void upsertAll(List<Article> articles);

    List<Article> findArticleInventory();

    Map<Integer, Article> findInventoryOf(Set<Integer> containigArticleIds);

    void removeFromStock(Map<Integer, Integer> idAmount);

}
