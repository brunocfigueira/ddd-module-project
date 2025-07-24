package hexagonal.modular.domain.ecommerce.core.usecases.orders;

import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.OrderDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.enums.OrderStatusEnum;
import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.OrderRepository;
import hexagonal.modular.domain.ecommerce.web.dtos.orders.UpdatePaymentKeyRequest;
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
class OrderPaymentKeyUseCaseTest {

    @Mock
    private OrderRepository repository;

    @InjectMocks
    private OrderPaymentKeyUseCase useCase;

    @Test
    @DisplayName("Should update payment key successfully")
    void shouldUpdatePaymentKeySuccessfully() {
        var request = new UpdatePaymentKeyRequest("order-123", "payment-key-123", OrderStatusEnum.APPROVED);
        var order = new OrderDocument();

        when(repository.findById("order-123")).thenReturn(Optional.of(order));

        useCase.execute(request);

        verify(repository).save(order);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when order not found")
    void shouldThrowEntityNotFoundExceptionWhenOrderNotFound() {
        var request = new UpdatePaymentKeyRequest("order-123", "payment-key-123", OrderStatusEnum.APPROVED);

        when(repository.findById("order-123")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> useCase.execute(request));
    }

    @Test
    @DisplayName("Should throw BusinessRuleException when request is null")
    void shouldThrowBusinessRuleExceptionWhenRequestIsNull() {
        assertThrows(BusinessRuleException.class, () -> useCase.execute(null));
    }
}
