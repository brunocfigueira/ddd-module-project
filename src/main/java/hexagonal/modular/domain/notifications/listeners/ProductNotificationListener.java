package hexagonal.modular.domain.notifications.listeners;

import hexagonal.modular.domain.ecommerce.events.ProductEvent;
import hexagonal.modular.domain.notifications.core.usecases.NotificationCreationUseCase;
import hexagonal.modular.domain.notifications.persistence.mongo.enums.NotificationTypeEnum;
import hexagonal.modular.domain.notifications.persistence.mongo.enums.RecipientTypeEnum;
import hexagonal.modular.domain.notifications.persistence.mongo.enums.SenderTypeEnum;
import hexagonal.modular.domain.notifications.web.dtos.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.HashMap;
import java.util.Map;

@Async
@Component
public class ProductNotificationListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductNotificationListener.class);

    private static final String MESSAGE = "A product registration has just been %s.";
    private final NotificationCreationUseCase notificationCreationUseCase;

    public ProductNotificationListener(NotificationCreationUseCase notificationCreationUseCase) {
        this.notificationCreationUseCase = notificationCreationUseCase;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @ApplicationModuleListener
    public void onProductEvent(ProductEvent event) {
        try {
            LOGGER.info("Received product {} event for ID: {}", event.type().name(), event.product().getId());

            var request = buildNotificationRequest(event);
            var notificationId = notificationCreationUseCase.execute(request);

            LOGGER.info("Notification created successfully: {}", notificationId);
        } catch (Exception ex) {
            LOGGER.error("Failed to process product event for ID: {}", event.product().getId(), ex);
            // TODO: Considere implementar retry logic ou dead letter queue
        }
    }

    private NotificationRequest buildNotificationRequest(ProductEvent event) {
        Map<String, Object> content = new HashMap<>();
        content.put("product", event.product());
        return new NotificationRequest(
                SenderTypeEnum.SYSTEM.name(),
                RecipientTypeEnum.USER.name(),
                event.product().getId(),
                NotificationTypeEnum.INTERNAL,
                event.type().getValue(),
                buildMessage(event),
                content
        );
    }

    private String buildMessage(ProductEvent event) {
        return switch (event.type()) {
            case RECORD_CREATION_NOTIFICATION -> String.format(MESSAGE, "created");
            case RECORD_UPDATE_NOTIFICATION -> String.format(MESSAGE, "updated");
            case RECORD_DELETE_NOTIFICATION -> String.format(MESSAGE, "deleted");
            default -> String.format(MESSAGE, "processed");
        };
    }
}
