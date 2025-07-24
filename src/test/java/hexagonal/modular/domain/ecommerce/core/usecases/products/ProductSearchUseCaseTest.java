
package hexagonal.modular.domain.ecommerce.core.usecases.products;

import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.ProductDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.ProductRepository;
import hexagonal.modular.domain.ecommerce.web.dtos.products.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductSearchUseCaseTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductSearchUseCase useCase;

    @Test
    @DisplayName("Should return a page of products")
    void shouldReturnAPageOfProducts() {
        var pageable = PageRequest.of(0, 10);
        var product = new ProductDocument();
        var page = new PageImpl<>(Collections.singletonList(product), pageable, 1);

        when(repository.findAll(pageable)).thenReturn(page);

        Page<ProductResponse> response = useCase.execute(pageable);

        assertNotNull(response);
    }
}
