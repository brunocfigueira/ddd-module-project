package hexagonal.modular.domain.sales.core.usecases;

import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.OrderDocument;
import hexagonal.modular.domain.ecommerce.web.dtos.orders.CreateOrderRequest;
import hexagonal.modular.domain.sales.dtos.ConfirmSaleRequest;
import hexagonal.modular.domain.sales.persitence.postgre.entities.SaleEntity;
import hexagonal.modular.domain.sales.persitence.postgre.repositories.SaleRepository;
import hexagonal.modular.shared.exceptions.BusinessRuleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConfirmationSaleUseCaseTest {

    @Mock
    private SaleRepository repository;

    @InjectMocks
    private ConfirmationSaleUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldConfirmSaleWhenRequestIsValid() {
        var request = new ConfirmSaleRequest(anyOrder());
        when(repository.save(any(SaleEntity.class))).thenReturn(SaleEntity.buildSale(request));

        useCase.execute(request);

        verify(repository).save(any(SaleEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenRequestIsNull() {
        assertThrows(BusinessRuleException.class, () -> useCase.execute(null));
    }

    @Test
    void shouldThrowExceptionWhenOrderIsNull() {
        var request = new ConfirmSaleRequest(null);
        assertThrows(BusinessRuleException.class, () -> useCase.execute(request));
    }

    private OrderDocument anyOrder() {
        return OrderDocument.buildOrderDocument(new CreateOrderRequest("cartId", "paymentMethodId", "shippingAddressId", "billingAddressId"));
    }
}