package hexagonal.modular.domain.sales.listeners;

import hexagonal.modular.domain.ecommerce.events.SaleEvent;
import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.OrderDocument;
import hexagonal.modular.domain.ecommerce.web.dtos.orders.CreateOrderRequest;
import hexagonal.modular.domain.sales.core.usecases.ConfirmationSaleUseCase;
import hexagonal.modular.domain.sales.dtos.ConfirmSaleRequest;
import hexagonal.modular.shared.enums.EventTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SaleListenerTest {

    @Mock
    private ConfirmationSaleUseCase confirmationSaleUseCase;

    @InjectMocks
    private SaleListener saleListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldHandleSaleEventSuccessfully() {
        var event = new SaleEvent(anyOrder(), EventTypeEnum.CONFIRMATION_SALE);
        doNothing().when(confirmationSaleUseCase).execute(any(ConfirmSaleRequest.class));

        assertDoesNotThrow(() -> saleListener.onSaleEvent(event));

        verify(confirmationSaleUseCase).execute(any(ConfirmSaleRequest.class));
    }

    @Test
    void shouldHandleExceptionWhenProcessingSaleEvent() {
        var event = new SaleEvent(anyOrder(), EventTypeEnum.CONFIRMATION_SALE);
        doThrow(new RuntimeException("Test Exception")).when(confirmationSaleUseCase).execute(any(ConfirmSaleRequest.class));

        assertDoesNotThrow(() -> saleListener.onSaleEvent(event));

        verify(confirmationSaleUseCase).execute(any(ConfirmSaleRequest.class));
    }

    private OrderDocument anyOrder() {
        return OrderDocument.buildOrderDocument(new CreateOrderRequest("cartId", "paymentMethodId", "shippingAddressId", "billingAddressId"));
    }
}