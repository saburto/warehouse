package com.saburto.warehouse.rest;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.util.List;

import com.saburto.warehouse.applications.InventoryLoader;
import com.saburto.warehouse.domain.entities.Article;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InventoryControllerTest {

    @Mock
    private InventoryLoader loader;

    @Captor
    private ArgumentCaptor<List<Article>> captor;

    @Test
    void recieve_invetory_dto_to_loader() {

        var controller = new InventoryController(loader);

        var e1 = new ArticleRequest(1, "product1", 10);
        var inventory = new InventoryRequest(List.of(e1));

        controller.load(inventory);

        verify(loader).load(captor.capture());

        var articles = captor.getValue();

        assertThat(articles)
            .hasSize(1).first()
            .extracting(Article::getId, Article::getName, Article::getStock)
            .contains(1, "product1", 10L);
    }

}
