package com.saburto.warehouse.applications;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.stream.IntStream;

import com.saburto.warehouse.domain.entities.Article;
import com.saburto.warehouse.domain.services.InventoryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InventoryLoaderTest {

    private InventoryLoader loader;

    @Mock
    private InventoryRepository repo;

    @BeforeEach
    void setup() {
        loader = new InventoryLoader(repo);
    }

    @Test
    void load_articles_successful() {
        var articles = createArticles(20);

        loader.load(articles);

        verify(repo).upsertAll(articles);

    }

    @Test
    void load_articles_failed() {

        var articles = createArticles(20);

        doThrow(new RuntimeException("Unknown Error"))
            .when(repo)
            .upsertAll(articles);

        assertThatThrownBy(() -> loader.load(articles))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Unknown Error");

    }

    private List<Article> createArticles(int total) {

        return IntStream.range(0, total)
            .mapToObj(i -> new Article(1, "name", 10))
            .collect(toList());
    }
}
