package hexagonal.modular.domain.ecommerce.core.usecases.products;

import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.ProductDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.ProductRepository;
import hexagonal.modular.domain.ecommerce.web.dtos.products.ProductResponse;
import hexagonal.modular.shared.exceptions.BusinessRuleException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductDetailsUseCaseTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductDetailsUseCase useCase;

    @Test
    @DisplayName("Should return product details successfully")
    void shouldReturnProductDetailsSuccessfully() {
        var product = new ProductDocument();
        when(repository.findById("product-123")).thenReturn(Optional.of(product));

        ProductResponse response = useCase.execute("product-123");

        assertNotNull(response);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when product not found")
    void shouldThrowEntityNotFoundExceptionWhenProductNotFound() {
        when(repository.findById("product-123")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> useCase.execute("product-123"));
    }

    @Test
    @DisplayName("Should throw BusinessRuleException when id is blank")
    void shouldThrowBusinessRuleExceptionWhenIdIsBlank() {
        assertThrows(BusinessRuleException.class, () -> useCase.execute(""));
    }
}
