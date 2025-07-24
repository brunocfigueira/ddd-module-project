package hexagonal.modular.domain.ecommerce.core.usecases.carts;

import hexagonal.modular.domain.ecommerce.persistence.mongo.enums.CartStatusEnum;
import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.CartRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartChangeStatusUseCaseTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartChangeStatusUseCase useCase;

    @Test
    @DisplayName("Should change cart status successfully")
    void shouldChangeCartStatusSuccessfully() {
        var cart = new hexagonal.modular.domain.ecommerce.persistence.mongo.documents.CartDocument();
        cart.setStatus(CartStatusEnum.OPENED);

        when(cartRepository.findById("cart-123")).thenReturn(Optional.of(cart));

        useCase.execute(new CartChangeStatusUseCase.Input("cart-123", CartStatusEnum.CHECKOUT));

        verify(cartRepository).save(cart);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when cart not found")
    void shouldThrowEntityNotFoundExceptionWhenCartNotFound() {
        when(cartRepository.findById("cart-123")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> useCase.execute(new CartChangeStatusUseCase.Input("cart-123", CartStatusEnum.CHECKOUT)));
    }

    @Test
    @DisplayName("Should throw BusinessRuleException when cart is finished")
    void shouldThrowBusinessRuleExceptionWhenCartIsFinished() {
        var cart = new hexagonal.modular.domain.ecommerce.persistence.mongo.documents.CartDocument();
        cart.setStatus(CartStatusEnum.FINISHED);

        when(cartRepository.findById("cart-123")).thenReturn(Optional.of(cart));

        assertThrows(BusinessRuleException.class, () -> useCase.execute(new CartChangeStatusUseCase.Input("cart-123", CartStatusEnum.CHECKOUT)));
    }

    @Test
    @DisplayName("Should throw BusinessRuleException when cart is canceled")
    void shouldThrowBusinessRuleExceptionWhenCartIsCanceled() {
        var cart = new hexagonal.modular.domain.ecommerce.persistence.mongo.documents.CartDocument();
        cart.setStatus(CartStatusEnum.CANCELED);

        when(cartRepository.findById("cart-123")).thenReturn(Optional.of(cart));

        assertThrows(BusinessRuleException.class, () -> useCase.execute(new CartChangeStatusUseCase.Input("cart-123", CartStatusEnum.CHECKOUT)));
    }

    @Test
    @DisplayName("Should throw BusinessRuleException when input is null")
    void shouldThrowBusinessRuleExceptionWhenInputIsNull() {
        assertThrows(BusinessRuleException.class, () -> useCase.execute(null));
    }
}
