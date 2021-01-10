package com.saburto.warehouse.applications;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Map;
import java.util.Optional;
import com.saburto.warehouse.domain.entities.Article;
import com.saburto.warehouse.domain.entities.ProductDefinition;
import com.saburto.warehouse.domain.services.InventoryRepository;
import com.saburto.warehouse.domain.services.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductSellerTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    InventoryRepository inventoryRepository;

    private ProductSeller seller;

    @BeforeEach
    void setup() {
        seller = new ProductSeller(productRepository, inventoryRepository);

        when(productRepository.getProducDefinition(anyString())).thenReturn(Optional.of(mock(ProductDefinition.class)));
    }

    @Test
    void sell_product_ok() {

        var productDefinition = new ProductDefinition("table a", Map.of(1, 3, 2, 1));
        var inventory = Map.of(1, new Article(1, "a", 3), 2, new Article(2, "b", 1));


        when(productRepository.getProducDefinition("table a")).thenReturn(Optional.of(productDefinition));
        when(inventoryRepository.findInventoryOf(anySet())).thenReturn(inventory);

        seller.sell("table a");

        verify(inventoryRepository).removeFromStock(Map.of(1, 3, 2, 1));

    }

    @Test
    void throw_exception_product_without_stock() {

        assertThatThrownBy(() -> seller.sell("no-stock-table"))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("No available stock for");

    }

    @Test
    void throw_exception_product_not_found() {

        when(productRepository.getProducDefinition(anyString()))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> seller.sell("no-stock-table"))
            .isInstanceOf(NoProductFoundException.class);

    }

}
