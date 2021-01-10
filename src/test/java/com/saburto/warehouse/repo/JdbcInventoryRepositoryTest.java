package com.saburto.warehouse.repo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Set;
import com.saburto.warehouse.domain.entities.Article;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration-test")
public class JdbcInventoryRepositoryTest extends AbstractMysqlContainerBaseTest {

    private JdbcInventoryRepository repo;

    @BeforeEach
    void setup() throws Exception {
        repo = new JdbcInventoryRepository(jdbcTemplate);
    }

    @Test
    void insert_articles_successfully() {

        repo.upsertAll(List.of(new Article(1, "abc", 100),
                               new Article(2, "dfg", 1000)));

        var count = jdbcTemplate.queryForObject("select count(*) from Article",
                                                Map.of(), Integer.class);

        assertThat(count).isEqualTo(2);

    }


    @Test
    void update_articles_successfully() {

        repo.upsertAll(List.of(new Article(5, "abc", 50)));

        repo.upsertAll(List.of(new Article(5, "abc", 100)));

        var stock = jdbcTemplate.queryForObject("select stock from Article where art_id = :id",
                                                Map.of("id", 5), Integer.class);

        assertThat(stock).isEqualTo(100);

    }

    @Test
    void find_all_inventory() {

        repo.upsertAll(List.of(new Article(1, "abc", 100), new Article(2, "dfg", 1000)));

        var inventory = repo.findArticleInventory();

        assertThat(inventory).hasSize(2);

    }

    @Test
    void find_inventory_of_one_article() {

        repo.upsertAll(List.of(new Article(1, "abc", 100), new Article(2, "dfg", 1000)));

        var mapInventory = repo.findInventoryOf(Set.of(1));

        assertThat(mapInventory).hasSize(1)
            .hasEntrySatisfying(1, v -> {
                    assertThat(v.getStock()).isEqualTo(100);
                });

    }

    @Test
    void find_inventory_of_two_articles() {

        repo.upsertAll(List.of(new Article(1, "abc", 100), new Article(2, "dfg", 1000)));

        var mapInventory = repo.findInventoryOf(Set.of(1, 2));

        assertThat(mapInventory).hasSize(2)
            .hasEntrySatisfying(1, v -> {
                    assertThat(v.getStock()).isEqualTo(100);
                })
            .hasEntrySatisfying(2, v -> {
                    assertThat(v.getStock()).isEqualTo(1000);
                });

    }


    @Test
    void remove_from_stock() {

        repo.upsertAll(List.of(new Article(1, "abc", 100), new Article(2, "dfg", 1000)));


        repo.removeFromStock(Map.of(1, 50));

        var mapInventory = repo.findInventoryOf(Set.of(1));

        assertThat(mapInventory).hasSize(1).hasEntrySatisfying(1, v -> {
            assertThat(v.getStock()).isEqualTo(50);
        });

    }


    @Test
    void remove_from_stock_two_articles() {

        repo.upsertAll(List.of(new Article(1, "abc", 100), new Article(2, "dfg", 1000)));

        repo.removeFromStock(Map.of(1, 50, 2, 200));

        var mapInventory = repo.findInventoryOf(Set.of(1, 2));

        assertThat(mapInventory).hasSize(2).hasEntrySatisfying(1, v -> {
            assertThat(v.getStock()).isEqualTo(50);
        }).hasEntrySatisfying(2, v -> {
            assertThat(v.getStock()).isEqualTo(800);
        });

    }

}
