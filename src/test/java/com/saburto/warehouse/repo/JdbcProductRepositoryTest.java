package com.saburto.warehouse.repo;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;
import com.saburto.warehouse.domain.entities.ProductDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

@Tag("integration-test")
public class JdbcProductRepositoryTest extends AbstractMysqlContainerBaseTest {

    private JdbcProductRepository repo;

    @BeforeEach
    void setup() throws Exception {
        repo = new JdbcProductRepository(jdbcTemplate);
    }

    @Test
    void insert_new_proudct_with_one_article() {

        insertArticle(1);
        var articles = Map.of(1, 2);

        var newProduct = new ProductDefinition("table-a", articles);
        var products = List.of(newProduct);
        repo.upsertAll(products);

        countProductInserted(products.size(), 1);
    }

    @Test
    void insert_new_proudct_with_multiple_articles() {

        var articles = IntStream.range(0, 10)
            .peek(i -> insertArticle(i))
            .mapToObj(Integer::valueOf)
            .collect(toMap(Function.identity(), (i) -> 2));

        var newProduct = new ProductDefinition("table-a", articles);
        var products = List.of(newProduct);
        repo.upsertAll(products);

        countProductInserted(products.size(), 10);
    }

    @Test
    void insert_multiple_proudcts() {

        insertArticle(1);
        var articles = Map.of(1, 2);

        var products = IntStream.range(0, 10).mapToObj(i -> new ProductDefinition("table-" + i, articles)).collect(toList());
        repo.upsertAll(products);

        countProductInserted(products.size(), 10);
    }

    @Test
    void insert_proudct_without_article_inserted() {

        var articles = Map.of(1, 2);

        var newProduct = new ProductDefinition("table-a", articles);
        var products = List.of(newProduct);

        assertThatThrownBy(() -> repo.upsertAll(products)).isInstanceOf(DataIntegrityViolationException.class);
    }


    @Test
    void find_product_definitions() {

        insertArticle(1);
        var articles = Map.of(1, 2);

        var newProduct = new ProductDefinition("table-a", articles);
        var products = List.of(newProduct);
        repo.upsertAll(products);

        var defs = repo.findProductDefinitions();

        assertThat(defs).hasSize(1)
            .first()
            .satisfies(d -> {
                    assertThat(d.getName()).isEqualTo("table-a");
                    assertThat(d.getArticlesAmount()).containsExactlyEntriesOf(articles);
                });

    }

    @Test
    void find_multiple_product_definitions() {

        insertArticle(1);
        var articles = Map.of(1, 2);

        var products = IntStream.range(0, 10)
                .mapToObj(i -> new ProductDefinition("table-" + i, articles)).collect(toList());
        repo.upsertAll(products);

        var defs = repo.findProductDefinitions();

        assertThat(defs).hasSize(products.size()).allSatisfy(d -> {
            assertThat(d.getName()).startsWith("table-");
            assertThat(d.getArticlesAmount()).containsExactlyEntriesOf(articles);
        });

    }

    @Test
    void find_proudct_with_multiple_articles() {

        var articles = IntStream.range(0, 10).peek(i -> insertArticle(i)).mapToObj(Integer::valueOf)
                .collect(toMap(Function.identity(), (i) -> 2));

        var newProduct = new ProductDefinition("table-a", articles);
        var products = List.of(newProduct);
        repo.upsertAll(products);

        var defs = repo.findProductDefinitions();

        assertThat(defs).hasSize(1).first().satisfies(d -> {
            assertThat(d.getName()).isEqualTo("table-a");
            assertThat(d.getArticlesAmount()).containsExactlyEntriesOf(articles);
        });
    }

    @Test
    void get_product_definition_by_name() {

        insertArticle(1);
        var articles = Map.of(1, 2);

        var newProduct = new ProductDefinition("table-a", articles);
        var products = List.of(newProduct);
        repo.upsertAll(products);

        var def = repo.getProducDefinition("table-a");


        assertThat(def).hasValueSatisfying(d -> {
                assertThat(d.getName()).isEqualTo("table-a");
                assertThat(d.getArticlesAmount()).containsExactlyEntriesOf(articles);
            });
    }

    @Test
    void get_product_definition_by_name_not_found_returns_empty() {

        var d = repo.getProducDefinition("table-a");

        assertThat(d).isEmpty();

    }

    private void countProductInserted(int products, int articles) {
        var count = jdbcTemplate.queryForObject("select count(*) from Product", Map.of(),
                Integer.class);

        assertThat(count).as("check products").isEqualTo(products);


        var countContainingArticles = jdbcTemplate.queryForObject(
                "select count(*) from Containing_Articles", Map.of(), Integer.class);

        assertThat(countContainingArticles).as("check articles").isEqualTo(articles);
    }

    private void insertArticle(int id) {
        jdbcTemplate.update("insert into Article values(:id, :name, :stock)",
                            Map.of("id", id,
                                   "name", "qwerty",
                                   "stock", 10));
    }

}
