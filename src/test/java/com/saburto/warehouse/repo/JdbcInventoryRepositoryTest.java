package com.saburto.warehouse.repo;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import com.saburto.warehouse.domain.entities.Article;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Tag("integration-test")
public class JdbcInventoryRepositoryTest {

    private JdbcInventoryRepository repo;
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Container
    private static final MySQLContainer MY_SQL_CONTAINER = new MySQLContainer("mysql:5.7.22");

    @BeforeEach
    void setup() throws Exception {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(MY_SQL_CONTAINER.getDriverClassName());
        dataSource.setUrl(MY_SQL_CONTAINER.getJdbcUrl());
        dataSource.setUsername(MY_SQL_CONTAINER.getUsername());
        dataSource.setPassword(MY_SQL_CONTAINER.getPassword());


        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String schema = Files.readString(Path.of(getClass().getResource("/schema.sql").toURI()));

        jdbcTemplate.update(schema, Map.of());

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

}
