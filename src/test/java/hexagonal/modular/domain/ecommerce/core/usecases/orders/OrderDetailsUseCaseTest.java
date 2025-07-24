package hexagonal.modular.domain.ecommerce.core.usecases.orders;

import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.OrderDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.OrderRepository;
import hexagonal.modular.domain.ecommerce.web.dtos.orders.OrderResponse;
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
class OrderDetailsUseCaseTest {

    @Mock
    private OrderRepository repository;

    @InjectMocks
    private OrderDetailsUseCase useCase;

    @Test
    @DisplayName("Should return order details successfully")
    void shouldReturnOrderDetailsSuccessfully() {
        var order = new OrderDocument();
        when(repository.findById("order-123")).thenReturn(Optional.of(order));

        OrderResponse response = useCase.execute("order-123");

        assertNotNull(response);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when order not found")
    void shouldThrowEntityNotFoundExceptionWhenOrderNotFound() {
        when(repository.findById("order-123")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> useCase.execute("order-123"));
    }

    @Test
    @DisplayName("Should throw BusinessRuleException when id is blank")
    void shouldThrowBusinessRuleExceptionWhenIdIsBlank() {
        assertThrows(BusinessRuleException.class, () -> useCase.execute(""));
    }
}
