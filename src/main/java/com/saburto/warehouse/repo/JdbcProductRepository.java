package com.saburto.warehouse.repo;

import static java.util.stream.Collectors.toList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.saburto.warehouse.domain.entities.ProductDefinition;
import com.saburto.warehouse.domain.services.ProductRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcProductRepository implements ProductRepository {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcProductRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void upsertAll(List<ProductDefinition> products) {
        upsertProducts(products);
        upsertContainingArticles(products);
    }

    @Override
    public List<ProductDefinition> findProductDefinitions() {
        var productMap = jdbcTemplate.queryForStream("select p.name name, c.art_id id, c.amount amount from Product p inner join Containing_Articles c on c.name_product = p.name",
                                                     Map.of(), this::toDefinition)
            .collect(Collectors.groupingBy(p -> p.getName()));

        return productMap.values()
            .stream()
            .map(l -> l.stream().reduce(ProductDefinition::merge).get())
            .collect(toList());

    }

    @Override
    public Optional<ProductDefinition> getProducDefinition(String name) {

        var productMap = jdbcTemplate.queryForStream(
                "select p.name name, c.art_id id, c.amount amount from Product p inner join Containing_Articles c on c.name_product = p.name where p.name = :name",
                Map.of("name", name), this::toDefinition).collect(Collectors.groupingBy(p -> p.getName()));

        return productMap.values().stream()
            .map(l -> l.stream().reduce(ProductDefinition::merge).get())
            .findFirst();
    }

    private ProductDefinition toDefinition(ResultSet rs, int rowNum) throws SQLException {
        return new ProductDefinition(rs.getString("name"), Map.of(rs.getInt("id"), rs.getInt("amount")));
    }


    private void upsertContainingArticles(List<ProductDefinition> products) {
        var paramsContainingArticles = products.stream()
            .flatMap(this::toContaingArticlesParams)
            .map(m -> new MapSqlParameterSource(m))
            .collect(toList());

        var paramContaingArticles = new SqlParameterSource[paramsContainingArticles.size()];

        jdbcTemplate.batchUpdate(
                "insert into Containing_Articles values(:name_product, :art_id, :amount) on duplicate key update amount=:amount",
                paramsContainingArticles.toArray(paramContaingArticles));
    }

    private Stream<Map<String, ?>> toContaingArticlesParams(ProductDefinition p) {
        return p.getArticlesAmount()
            .entrySet()
            .stream()
            .map(e -> Map.of("name_product", p.getName(),
                             "art_id", e.getKey(),
                             "amount", e.getValue()));
    }

    private void upsertProducts(List<ProductDefinition> products) {
        var params = products.stream().map(a -> Map.of("name", a.getName()))
                .map(m -> new MapSqlParameterSource(m)).collect(toList());

        var param = new SqlParameterSource[params.size()];

        jdbcTemplate.batchUpdate(
                "insert into Product values(:name) on duplicate key update name=:name",
                params.toArray(param));
    }



}
