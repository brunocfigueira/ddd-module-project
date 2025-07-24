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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationsAllUseCaseTest {

    @Mock
    private NotificationRepository repository;

    @InjectMocks
    private NotificationsAllUseCase useCase;

    private NotificationDocument fakeNotificationDocument() {
        var doc = new NotificationDocument();
        doc.setId("notification-123");
        doc.setType(NotificationTypeEnum.INTERNAL);
        doc.setRead(false);
        doc.setCreatedAt(LocalDateTime.MAX);
        return doc;
    }

    @Test
    @DisplayName("Should return all notifications")
    void shouldReturnAllNotifications() {
        var notification = fakeNotificationDocument();
        Page<NotificationDocument> page = new PageImpl<>(List.of(notification));
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAllByOrderByCreatedAtDesc(pageable)).thenReturn(page);

        Page<NotificationResponse> result = useCase.execute(pageable);

        assertEquals(1, result.getTotalElements());
    }
}
