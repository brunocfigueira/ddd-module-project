package hexagonal.modular.domain.notifications.core.usecases;

import hexagonal.modular.domain.notifications.persistence.mongo.documents.NotificationDocument;
import hexagonal.modular.domain.notifications.persistence.mongo.enums.NotificationTypeEnum;
import hexagonal.modular.domain.notifications.persistence.mongo.repositories.NotificationRepository;
import hexagonal.modular.domain.notifications.web.dtos.NotificationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationsByReadStatusUseCaseTest {

    @Mock
    private NotificationRepository repository;

    @InjectMocks
    private NotificationsByReadStatusUseCase useCase;

    private NotificationDocument fakeNotificationDocument() {
        var doc = new NotificationDocument();
        doc.setId("notification-123");
        doc.setType(NotificationTypeEnum.INTERNAL);
        doc.setRead(true);
        doc.setCreatedAt(LocalDateTime.MAX);
        return doc;
    }

    @Test
    @DisplayName("Should return notifications by read status")
    void shouldReturnNotificationsByReadStatus() {
        var notification = fakeNotificationDocument();
        when(repository.findByRead(true)).thenReturn(List.of(notification));

        List<NotificationResponse> result = useCase.execute(true);

        assertEquals(1, result.size());
    }
}
