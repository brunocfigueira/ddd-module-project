package hexagonal.modular.domain.payments.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.exception.StripeException;
import hexagonal.modular.domain.ecommerce.core.usecases.orders.OrderPaymentKeyUseCase;
import hexagonal.modular.domain.ecommerce.events.PaymentEvent;
import hexagonal.modular.domain.ecommerce.events.SaleEvent;
import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.OrderDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.enums.OrderStatusEnum;
import hexagonal.modular.domain.ecommerce.web.dtos.orders.UpdatePaymentKeyRequest;
import hexagonal.modular.domain.payments.core.usecases.PaymentUseCase;
import hexagonal.modular.domain.payments.dtos.PaymentRequest;
import hexagonal.modular.domain.payments.dtos.PaymentResponse;
import hexagonal.modular.domain.payments.enums.PaymentStatus;
import hexagonal.modular.shared.enums.EventTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentListenerTest {

    @Mock
    private PaymentUseCase paymentUseCase;

    @Mock
    private OrderPaymentKeyUseCase orderPaymentKeyUseCase;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private PaymentListener paymentListener;

    private PaymentEvent paymentEvent;
    private OrderDocument orderDocument;

    @BeforeEach
    void setUp() {
        orderDocument = new OrderDocument();
        orderDocument.setId("order-123");
        paymentEvent = new PaymentEvent(orderDocument, EventTypeEnum.PAYMENT_REQUEST);
    }

    @Test
    @DisplayName("Should process payment event and publish sale event on success")
    void shouldProcessPaymentEventAndPublishSaleEventOnSuccess() throws StripeException, JsonProcessingException {
        var paymentResponse = new PaymentResponse(PaymentStatus.APPROVED, "pi_123", "cs_123");

        when(paymentUseCase.execute(any(PaymentRequest.class))).thenReturn(paymentResponse);

        paymentListener.onPaymentEvent(paymentEvent);

        verify(orderPaymentKeyUseCase).execute(any(UpdatePaymentKeyRequest.class));
        verify(eventPublisher).publishEvent(any(SaleEvent.class));
    }

    @Test
    @DisplayName("Should handle JsonProcessingException and update payment status to FAILED")
    void shouldHandleJsonProcessingExceptionAndUpdatePaymentStatusToFailed() throws StripeException, JsonProcessingException {
        when(paymentUseCase.execute(any(PaymentRequest.class))).thenThrow(JsonProcessingException.class);

        paymentListener.onPaymentEvent(paymentEvent);

        verify(orderPaymentKeyUseCase).execute(argThat(request ->
                request.status().equals(OrderStatusEnum.PENDING) &&
                        request.paymentReferenceKey() == null
        ));
        verify(eventPublisher, never()).publishEvent(any(SaleEvent.class));
    }

    @Test
    @DisplayName("Should update order status to CANCELED when payment is canceled")
    void shouldUpdateOrderStatusToCanceledWhenPaymentIsCanceled() throws StripeException, JsonProcessingException {
        var paymentResponse = new PaymentResponse(PaymentStatus.CANCELED, "pi_123", "cs_123");

        when(paymentUseCase.execute(any(PaymentRequest.class))).thenReturn(paymentResponse);

        paymentListener.onPaymentEvent(paymentEvent);

        verify(orderPaymentKeyUseCase).execute(argThat(request ->
                request.status().equals(OrderStatusEnum.CANCELED)
        ));
        verify(eventPublisher).publishEvent(any(SaleEvent.class));
    }
}
