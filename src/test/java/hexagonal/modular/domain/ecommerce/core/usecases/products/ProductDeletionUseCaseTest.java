package hexagonal.modular.domain.ecommerce.core.usecases.products;

import hexagonal.modular.domain.ecommerce.events.ProductEvent;
import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.ProductDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.ProductRepository;
import hexagonal.modular.shared.exceptions.BusinessRuleException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductDeletionUseCaseTest {

    @Mock
    private ApplicationEventPublisher eventListener;

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductDeletionUseCase useCase;

    @Test
    @DisplayName("Should delete product successfully")
    void shouldDeleteProductSuccessfully() {
        var product = new ProductDocument();
        when(repository.findById("product-123")).thenReturn(Optional.of(product));

        useCase.execute("product-123");

        verify(repository).delete(product);
        verify(eventListener).publishEvent(any(ProductEvent.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when product not found")
    void shouldThrowEntityNotFoundExceptionWhenProductNotFound() {
        when(repository.findById("product-123")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> useCase.execute("product-123"));

        verify(repository, never()).delete(any());
        verify(eventListener, never()).publishEvent(any());
    }

    @Test
    @DisplayName("Should throw BusinessRuleException when id is blank")
    void shouldThrowBusinessRuleExceptionWhenIdIsBlank() {
        assertThrows(BusinessRuleException.class, () -> useCase.execute(""));
    }
}
