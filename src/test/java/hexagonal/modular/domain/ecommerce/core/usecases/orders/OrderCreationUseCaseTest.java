package hexagonal.modular.domain.ecommerce.core.usecases.orders;

import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.CartDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.OrderDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.enums.CartStatusEnum;
import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.CartRepository;
import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.OrderRepository;
import hexagonal.modular.domain.ecommerce.web.dtos.orders.CreateOrderRequest;
import hexagonal.modular.shared.exceptions.BusinessRuleException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderCreationUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private OrderCreationUseCase useCase;

    private CreateOrderRequest fakeRequest(String cartId) {
        return new CreateOrderRequest(cartId, "payment-123", "shipping-456", "billing-789");
    }

    @Test
    @DisplayName("Should create order successfully")
    void shouldCreateOrderSuccessfully() {
        var request = fakeRequest("cart-123");
        var cart = new CartDocument();
        cart.setStatus(CartStatusEnum.CHECKOUT);

        when(cartRepository.findById("cart-123")).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(OrderDocument.class))).thenReturn(any(OrderDocument.class));

        useCase.execute(request);

        verify(orderRepository).save(any(OrderDocument.class));
        verify(cartRepository).save(cart);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when cart not found")
    void shouldThrowEntityNotFoundExceptionWhenCartNotFound() {
        var request = fakeRequest("cart-123");

        when(cartRepository.findById("cart-123")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> useCase.execute(request));
    }

    @Test
    @DisplayName("Should throw BusinessRuleException when cart is not in checkout")
    void shouldThrowBusinessRuleExceptionWhenCartIsNotInCheckout() {
        var request = fakeRequest("cart-123");
        var cart = new CartDocument();
        cart.setStatus(CartStatusEnum.OPENED);

        when(cartRepository.findById("cart-123")).thenReturn(Optional.of(cart));

        assertThrows(BusinessRuleException.class, () -> useCase.execute(request));
    }
}
