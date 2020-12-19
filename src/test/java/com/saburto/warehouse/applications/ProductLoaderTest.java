package com.saburto.warehouse.applications;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import java.util.List;
import com.saburto.warehouse.applications.ProductLoader.NoArticleDefinedException;
import com.saburto.warehouse.domain.entities.ProductDefinition;
import com.saburto.warehouse.domain.services.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
public class ProductLoaderTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductDefinition productDefinition;

    private ProductLoader loader;

    private List<ProductDefinition> products;


    @BeforeEach
    void setup() {
        loader = new ProductLoader(repository);
        products = List.of(productDefinition);
    }


    @Test
    void load_products_successful() {
        loader.load(products);

        verify(repository).upsertAll(products);
    }

    @Test
    void load_products_with_exception() {

        doThrow(RuntimeException.class)
            .when(repository)
            .upsertAll(products);

        assertThatThrownBy(() -> loader.load(products))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    void load_products_with_and_article_not_found() {

        doThrow(DataIntegrityViolationException.class).when(repository).upsertAll(products);

        assertThatThrownBy(() -> loader.load(products)).isInstanceOf(NoArticleDefinedException.class);
    }

}
