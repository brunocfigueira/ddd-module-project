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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Async
@Component
public class PaymentListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentListener.class);

    private final PaymentUseCase paymentUseCase;
    private final OrderPaymentKeyUseCase orderPaymentKeyUseCase;
    private final ApplicationEventPublisher eventPublisher;

    public PaymentListener(PaymentUseCase paymentUseCase, OrderPaymentKeyUseCase orderPaymentKeyUseCase, ApplicationEventPublisher eventPublisher) {
        this.paymentUseCase = paymentUseCase;
        this.orderPaymentKeyUseCase = orderPaymentKeyUseCase;
        this.eventPublisher = eventPublisher;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @ApplicationModuleListener
    public void onPaymentEvent(PaymentEvent event) {
        try {
            LOGGER.info("Received payment {} event for order ID: {}", event.type().name(), event.order().getId());
            var payResponse = paymentUseCase.execute(new PaymentRequest(event.order()));

            updatePaymentKeyAndStatus(event.order(), payResponse);

            // TODO: Considerar implementar lógica de confirmação de venda com evento assíncrono
            eventPublisher.publishEvent(new SaleEvent(event.order(), EventTypeEnum.CONFIRMATION_SALE));

        } catch (StripeException | JsonProcessingException ex) {
            LOGGER.error("Failed to process payment event for order ID: {}", event.order().getId(), ex);
            // TODO: Considere implementar retry logic ou dead letter queue

            updatePaymentKeyAndStatus(event.order(), new PaymentResponse(PaymentStatus.FAILED, null, null));
        }
    }

    private void updatePaymentKeyAndStatus(OrderDocument order, PaymentResponse pay) {
        var orderStatus = OrderStatusEnum.PENDING;

        switch (pay.status()) {
            case PaymentStatus.APPROVED -> orderStatus = OrderStatusEnum.APPROVED;
            case PaymentStatus.CANCELED -> orderStatus = OrderStatusEnum.CANCELED;
        }

        orderPaymentKeyUseCase.execute(new UpdatePaymentKeyRequest(order.getId(), pay.paymentReferenceKey(), orderStatus));
    }
}
