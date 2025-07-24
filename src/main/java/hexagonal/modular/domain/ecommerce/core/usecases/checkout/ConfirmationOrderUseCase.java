package hexagonal.modular.domain.ecommerce.core.usecases.checkout;

import hexagonal.modular.domain.ecommerce.events.PaymentEvent;
import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.OrderDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.enums.OrderStatusEnum;
import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.OrderRepository;
import hexagonal.modular.shared.enums.EventTypeEnum;
import hexagonal.modular.shared.exceptions.BusinessRuleException;
import hexagonal.modular.shared.usecases.IUnitUseCase;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfirmationOrderUseCase implements IUnitUseCase<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmationOrderUseCase.class);
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ConfirmationOrderUseCase(OrderRepository orderRepository, ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public void execute(String orderId) {
        LOGGER.info("Starting order confirmation with ID: {}", orderId);
        orderRepository.findById(orderId).ifPresentOrElse(order -> {

            checkOrderStatus(order);
            order.setStatus(OrderStatusEnum.PENDING);
            orderRepository.save(order);
            LOGGER.info("Order updated to pending");

            // publish an event for generate payment
            publishEvent(order);

        }, () -> {
            throw new EntityNotFoundException("Order with ID " + orderId + " not found");
        });
    }

    private void checkOrderStatus(OrderDocument order) {
        if (!order.getStatus().equals(OrderStatusEnum.PENDING) && !order.getStatus().equals(OrderStatusEnum.CREATED)) {
            throw new BusinessRuleException("Order is not in a valid state for confirmation. Current Status: " + order.getStatus());
        }
    }

    private void publishEvent(OrderDocument order) {
        LOGGER.info("Publishing payment event for order ID: {}", order.getId());
        eventPublisher.publishEvent(new PaymentEvent(order, EventTypeEnum.PAYMENT_REQUEST));
    }
}
