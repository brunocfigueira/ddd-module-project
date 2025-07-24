
package hexagonal.modular.domain.ecommerce.core.usecases.products;

import hexagonal.modular.domain.ecommerce.events.ProductEvent;
import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.ProductDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.ProductRepository;
import hexagonal.modular.domain.ecommerce.web.dtos.products.UpdateProductRequest;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductUpdateUseCaseTest {

    @Mock
    private ApplicationEventPublisher eventListener;

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductUpdateUseCase useCase;

    @Test
    @DisplayName("Should update product successfully")
    void shouldUpdateProductSuccessfully() {
        var request = new UpdateProductRequest("product-123", "New Name", BigDecimal.ONE, false);
        var product = new ProductDocument();

        when(repository.findById("product-123")).thenReturn(Optional.of(product));

        useCase.execute(request);

        verify(repository).save(product);
        verify(eventListener).publishEvent(any(ProductEvent.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when product not found")
    void shouldThrowEntityNotFoundExceptionWhenProductNotFound() {
        var request = new UpdateProductRequest("product-123", "New Name", BigDecimal.ONE, false);

        when(repository.findById("product-123")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> useCase.execute(request));

        verify(repository, never()).save(any());
        verify(eventListener, never()).publishEvent(any());
    }
}
