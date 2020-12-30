package com.saburto.warehouse.domain.entities;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    void product_without_article_inventory() {

        var articleDef = new ProductDefinition.ArticleDefinition(1, 10);
        var definition = new ProductDefinition("table a", List.of(articleDef));

        var product = new Product(definition, Map.of());

        assertThat(product.getStock()).isEqualTo(0);

    }

    @Test
    void product_with_one_article_with_stock_in_inventory() {

        var articleDef = new ProductDefinition.ArticleDefinition(1, 1);
        var definition = new ProductDefinition("table a", List.of(articleDef));

        var article = new Article(1, "art", 1);
        var product = new Product(definition, Map.of(1, article));

        assertThat(product.getStock()).isEqualTo(1);

    }

    @Test
    void product_with_one_article_with_no_enough_stock_in_inventory() {

        var articleDef = new ProductDefinition.ArticleDefinition(1, 2);
        var definition = new ProductDefinition("table a", List.of(articleDef));

        var article = new Article(1, "art", 1);
        var product = new Product(definition, Map.of(1, article));

        assertThat(product.getStock()).isEqualTo(0);

    }

    @Test
    void product_with_multiple_article_one_not_in_inventory() {

        var articleDef1 = new ProductDefinition.ArticleDefinition(1, 1);
        var articleDef2 = new ProductDefinition.ArticleDefinition(2, 1);
        var definition = new ProductDefinition("table a", List.of(articleDef1, articleDef2));

        var article1 = new Article(1, "art", 1);
        var product = new Product(definition, Map.of(1, article1));

        assertThat(product.getStock()).isEqualTo(0);

    }

    @Test
    void product_with_multiple_article_one_without_stock_in_inventory() {

        var articleDef1 = new ProductDefinition.ArticleDefinition(1, 1);
        var articleDef2 = new ProductDefinition.ArticleDefinition(2, 1);
        var definition = new ProductDefinition("table a",
                                               List.of(articleDef1, articleDef2));

        var article1 = new Article(1, "art", 1);
        var article2 = new Article(2, "art 2", 0);
        var product = new Product(definition, Map.of(1, article1, 2, article2));

        assertThat(product.getStock()).isEqualTo(0);

    }

    @Test
    void product_with_multiple_article_one_with_stock_in_inventory() {

        var articleDef1 = new ProductDefinition.ArticleDefinition(1, 1);
        var articleDef2 = new ProductDefinition.ArticleDefinition(2, 1);
        var definition = new ProductDefinition("table a", List.of(articleDef1, articleDef2));

        var article1 = new Article(1, "art", 1);
        var article2 = new Article(2, "art 2", 1);
        var product = new Product(definition, Map.of(1, article1, 2, article2));

        assertThat(product.getStock()).isEqualTo(1);

    }

    @Test
    void product_with_multiple_article_but_different_article_stock_return_min_possible() {

        var articleDef1 = new ProductDefinition.ArticleDefinition(1, 1);
        var articleDef2 = new ProductDefinition.ArticleDefinition(2, 4);
        var definition = new ProductDefinition("table a", List.of(articleDef1, articleDef2));

        var article1 = new Article(1, "art", 10000);
        var article2 = new Article(2, "art 2", 8);
        var product = new Product(definition, Map.of(1, article1, 2, article2));

        assertThat(product.getStock()).isEqualTo(2);

    }

}
