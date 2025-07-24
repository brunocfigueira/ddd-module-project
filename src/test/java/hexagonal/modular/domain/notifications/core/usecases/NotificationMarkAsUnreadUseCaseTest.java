package hexagonal.modular.domain.notifications.core.usecases;

import hexagonal.modular.domain.notifications.persistence.mongo.documents.NotificationDocument;
import hexagonal.modular.domain.notifications.persistence.mongo.repositories.NotificationRepository;
import hexagonal.modular.shared.exceptions.BusinessRuleException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationMarkAsUnreadUseCaseTest {

    @Mock
    private NotificationRepository repository;

    @InjectMocks
    private NotificationMarkAsUnreadUseCase useCase;

    @Test
    @DisplayName("Should mark notification as unread successfully")
    void shouldMarkNotificationAsUnreadSuccessfully() {
        var notification = new NotificationDocument();
        notification.setRead(true);

        when(repository.findById("notification-123")).thenReturn(Optional.of(notification));

        useCase.execute("notification-123");

        assertFalse(notification.isRead());
        verify(repository).save(notification);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when notification not found")
    void shouldThrowEntityNotFoundExceptionWhenNotificationNotFound() {
        when(repository.findById("notification-123")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> useCase.execute("notification-123"));

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw BusinessRuleException when id is blank")
    void shouldThrowBusinessRuleExceptionWhenIdIsBlank() {
        assertThrows(BusinessRuleException.class, () -> useCase.execute(""));
    }
}
