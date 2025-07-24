
package hexagonal.modular.domain.notifications.core.usecases;

import hexagonal.modular.domain.notifications.persistence.mongo.documents.NotificationDocument;
import hexagonal.modular.domain.notifications.persistence.mongo.repositories.NotificationRepository;
import hexagonal.modular.domain.notifications.web.dtos.NotificationRequest;
import hexagonal.modular.domain.notifications.persistence.mongo.enums.NotificationTypeEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationCreationUseCaseTest {

    @Mock
    private NotificationRepository repository;

    @InjectMocks
    private NotificationCreationUseCase useCase;

    @Test
    @DisplayName("Should create notification successfully")
    void shouldCreateNotificationSuccessfully() {
        var request = new NotificationRequest(
                "SYSTEM",
                "USER",
                "user-123",
                NotificationTypeEnum.INTERNAL,
                "Test Subject",
                "Test Message",
                new HashMap<>()
        );

        var document = NotificationDocument.buildNotification(request);
        when(repository.save(any(NotificationDocument.class))).thenReturn(document);

        var notificationId = useCase.execute(request);

        assertEquals(document.getId(), notificationId);
        verify(repository).save(any(NotificationDocument.class));
    }
}
