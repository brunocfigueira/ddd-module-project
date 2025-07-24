package hexagonal.modular.domain.ecommerce.core.usecases.carts;

import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.CartDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.CartItems;
import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.CartRepository;
import hexagonal.modular.domain.ecommerce.web.dtos.carts.CartResponse;
import hexagonal.modular.shared.exceptions.BusinessRuleException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartDetailsUseCaseTest {

    @Mock
    private CartRepository repository;

    @InjectMocks
    private CartDetailsUseCase useCase;

    @Test
    @DisplayName("Should return cart details successfully")
    void shouldReturnCartDetailsSuccessfully() {
        var cart = new CartDocument();
        var cartItems = new CartItems();
        cart.setItems(List.of(cartItems));
        when(repository.findById("cart-123")).thenReturn(Optional.of(cart));

        CartResponse response = useCase.execute("cart-123");

        assertNotNull(response);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when cart not found")
    void shouldThrowEntityNotFoundExceptionWhenCartNotFound() {
        when(repository.findById("cart-123")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> useCase.execute("cart-123"));
    }

    @Test
    @DisplayName("Should throw BusinessRuleException when id is null")
    void shouldThrowBusinessRuleExceptionWhenIdIsNull() {
        assertThrows(BusinessRuleException.class, () -> useCase.execute(null));
    }
}
