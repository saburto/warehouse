package com.saburto.warehouse.repo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.MySQLContainer;

public class AbstractMysqlContainerBaseTest {

    public static final MySQLContainer MY_SQL_CONTAINER;

    static {
        MY_SQL_CONTAINER = new MySQLContainer("mysql:5.7.22");
        MY_SQL_CONTAINER.start();
    }

    NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    final void setupDatabase() throws Exception {

        var dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(MY_SQL_CONTAINER.getDriverClassName());
        dataSource.setUrl(MY_SQL_CONTAINER.getJdbcUrl());
        dataSource.setUsername(MY_SQL_CONTAINER.getUsername());
        dataSource.setPassword(MY_SQL_CONTAINER.getPassword());


        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String schema = Files.readString(Path.of(getClass().getResource("/schema.sql").toURI()));

        Arrays.stream(schema.split(";"))
            .filter(s -> !s.isBlank())
            .forEach(s -> jdbcTemplate.getJdbcTemplate().execute(s));

        jdbcTemplate.update("delete from Containing_Articles", Map.of());
        jdbcTemplate.update("delete from Article", Map.of());
        jdbcTemplate.update("delete from Product", Map.of());

    }

}
