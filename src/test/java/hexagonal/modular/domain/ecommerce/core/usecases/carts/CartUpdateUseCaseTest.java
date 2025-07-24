package hexagonal.modular.domain.ecommerce.core.usecases.carts;

import hexagonal.modular.domain.ecommerce.core.rules.ICartRule;
import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.CartDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.CartRepository;
import hexagonal.modular.domain.ecommerce.web.dtos.carts.CartItemsRequest;
import hexagonal.modular.domain.ecommerce.web.dtos.carts.UpdateCartRequest;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartUpdateUseCaseTest {

    @Mock
    private ApplicationEventPublisher eventListener;

    @Mock
    private CartRepository repository;

    @Mock
    private List<ICartRule> rules;

    @InjectMocks
    private CartUpdateUseCase useCase;

    @Test
    @DisplayName("Should update cart successfully")
    void shouldUpdateCartSuccessfully() {
        var cartItemsRequest = new CartItemsRequest("item-123", 2);
        var request = new UpdateCartRequest("cart-123", List.of(cartItemsRequest));
        var cart = new CartDocument();
        cart.setItems(new ArrayList<>());

        when(repository.findById("cart-123")).thenReturn(Optional.of(cart));

        useCase.execute(request);

        verify(repository).save(cart);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when cart not found")
    void shouldThrowEntityNotFoundExceptionWhenCartNotFound() {
        var request = new UpdateCartRequest("cart-123", Collections.emptyList());

        when(repository.findById("cart-123")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> useCase.execute(request));
    }
}
