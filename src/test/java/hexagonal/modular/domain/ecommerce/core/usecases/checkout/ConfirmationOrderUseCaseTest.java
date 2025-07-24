
package hexagonal.modular.domain.ecommerce.core.usecases.checkout;

import hexagonal.modular.domain.ecommerce.events.PaymentEvent;
import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.OrderDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.enums.OrderStatusEnum;
import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.OrderRepository;
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
class ConfirmationOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ConfirmationOrderUseCase useCase;

    @Test
    @DisplayName("Should confirm order successfully")
    void shouldConfirmOrderSuccessfully() {
        var order = new OrderDocument();
        order.setStatus(OrderStatusEnum.CREATED);

        when(orderRepository.findById("order-123")).thenReturn(Optional.of(order));

        useCase.execute("order-123");

        verify(orderRepository).save(order);
        verify(eventPublisher).publishEvent(any(PaymentEvent.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when order not found")
    void shouldThrowEntityNotFoundExceptionWhenOrderNotFound() {
        when(orderRepository.findById("order-123")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> useCase.execute("order-123"));
    }

    @Test
    @DisplayName("Should throw BusinessRuleException when order status is not valid")
    void shouldThrowBusinessRuleExceptionWhenOrderStatusIsNotValid() {
        var order = new OrderDocument();
        order.setStatus(OrderStatusEnum.APPROVED);

        when(orderRepository.findById("order-123")).thenReturn(Optional.of(order));

        assertThrows(BusinessRuleException.class, () -> useCase.execute("order-123"));
    }
}
