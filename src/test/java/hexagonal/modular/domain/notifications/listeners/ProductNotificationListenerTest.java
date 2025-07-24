package hexagonal.modular.domain.notifications.listeners;

import hexagonal.modular.domain.ecommerce.events.ProductEvent;
import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.ProductDocument;
import hexagonal.modular.domain.notifications.core.usecases.NotificationCreationUseCase;
import hexagonal.modular.domain.notifications.web.dtos.NotificationRequest;
import hexagonal.modular.shared.enums.EventTypeEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductNotificationListenerTest {

    @Mock
    private NotificationCreationUseCase notificationCreationUseCase;

    @InjectMocks
    private ProductNotificationListener listener;

    @Test
    @DisplayName("Should handle product creation event")
    void shouldHandleProductCreationEvent() {
        var product = new ProductDocument();
        product.setId("product-123");
        var event = new ProductEvent(product, EventTypeEnum.RECORD_CREATION_NOTIFICATION);

        when(notificationCreationUseCase.execute(any(NotificationRequest.class))).thenReturn("notification-123");

        listener.onProductEvent(event);

        verify(notificationCreationUseCase).execute(any(NotificationRequest.class));
    }

    @Test
    @DisplayName("Should handle product update event")
    void shouldHandleProductUpdateEvent() {
        var product = new ProductDocument();
        product.setId("product-123");
        var event = new ProductEvent(product, EventTypeEnum.RECORD_UPDATE_NOTIFICATION);

        when(notificationCreationUseCase.execute(any(NotificationRequest.class))).thenReturn("notification-123");

        listener.onProductEvent(event);

        verify(notificationCreationUseCase).execute(any(NotificationRequest.class));
    }

    @Test
    @DisplayName("Should handle product delete event")
    void shouldHandleProductDeleteEvent() {
        var product = new ProductDocument();
        product.setId("product-123");
        var event = new ProductEvent(product, EventTypeEnum.RECORD_DELETE_NOTIFICATION);

        when(notificationCreationUseCase.execute(any(NotificationRequest.class))).thenReturn("notification-123");

        listener.onProductEvent(event);

        verify(notificationCreationUseCase).execute(any(NotificationRequest.class));
    }

    @Test
    @DisplayName("Should handle exception during event processing")
    void shouldHandleExceptionDuringEventProcessing() {
        var product = new ProductDocument();
        product.setId("product-123");
        var event = new ProductEvent(product, EventTypeEnum.RECORD_CREATION_NOTIFICATION);

        when(notificationCreationUseCase.execute(any(NotificationRequest.class))).thenThrow(new RuntimeException("Test Exception"));

        listener.onProductEvent(event);

        verify(notificationCreationUseCase).execute(any(NotificationRequest.class));
    }
}
