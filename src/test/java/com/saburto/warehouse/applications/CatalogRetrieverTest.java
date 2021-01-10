package com.saburto.warehouse.applications;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Map;
import com.saburto.warehouse.domain.entities.Article;
import com.saburto.warehouse.domain.entities.Product;
import com.saburto.warehouse.domain.entities.ProductDefinition;
import com.saburto.warehouse.domain.services.InventoryRepository;
import com.saburto.warehouse.domain.services.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CatalogRetrieverTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    private CatalogRetriever catalogRetriever;

    @BeforeEach
    void setup() {
        catalogRetriever = new CatalogRetriever(productRepository, inventoryRepository);

        when(inventoryRepository.findArticleInventory()).thenReturn(List.of());
    }


    @Test
    void retrieve_empty_catalog() {

        var catalog = catalogRetriever.getCatalog();

        assertThat(catalog).isNotNull();
        assertThat(catalog.getProducts()).isEmpty();
    }

    @Test
    void get_product_table_a_without_stock() {
        var product1 = newProductDefinition();
        when(productRepository.findProductDefinitions())
            .thenReturn(List.of(product1));

        var catalog = catalogRetriever.getCatalog();

        assertThat(catalog).isNotNull();
        assertThat(catalog.getProducts()).isNotEmpty()
            .first()
            .extracting(Product::getName, Product::getStock)
            .contains("table a", 0);
    }



    @Test
    void get_product_table_a_with_stock() {

        var article = new Article(1, "art", 10);
        when(inventoryRepository.findArticleInventory()).thenReturn(List.of(article));

        var product1 = newProductDefinition();
        when(productRepository.findProductDefinitions()).thenReturn(List.of(product1));

        var catalog = catalogRetriever.getCatalog();

        assertThat(catalog).isNotNull();
        assertThat(catalog.getProducts()).isNotEmpty()
            .first()
            .extracting(Product::getName, Product::getStock)
            .contains("table a", 10);
    }

    private ProductDefinition newProductDefinition() {
        return new ProductDefinition("table a", Map.of(1, 1));
    }

}
