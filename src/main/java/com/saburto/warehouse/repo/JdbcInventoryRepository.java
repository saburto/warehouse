package com.saburto.warehouse.repo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.saburto.warehouse.domain.entities.Article;
import com.saburto.warehouse.domain.services.InventoryRepository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class JdbcInventoryRepository implements InventoryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcInventoryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void upsertAll(List<Article> articles) {

        var params = articles.stream()
            .map(a -> Map.of("id", a.getId(),
                             "name", a.getName(),
                             "stock", a.getStock()))
            .map(m -> new MapSqlParameterSource(m))
            .collect(Collectors.toList());

        SqlParameterSource[] param = new SqlParameterSource[params.size()];

        jdbcTemplate.batchUpdate("insert into Article values(:id, :name, :stock) on duplicate key update name=:name, stock=:stock",
                                 params.toArray(param));

    }

    @Override
    public List<Article> findArticleInventory() {
        return jdbcTemplate.query("select art_id, name, stock from Article", this::toArticle);
    }

    private Article toArticle(ResultSet result, int rowNum) throws SQLException {
        return new Article(result.getInt("art_id"), result.getString("name"), result.getLong("stock"));
    }

}
