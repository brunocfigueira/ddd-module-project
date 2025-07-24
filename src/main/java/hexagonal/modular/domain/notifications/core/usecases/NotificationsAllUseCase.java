package hexagonal.modular.domain.notifications.core.usecases;

import hexagonal.modular.domain.notifications.persistence.mongo.documents.NotificationDocument;
import hexagonal.modular.domain.notifications.persistence.mongo.repositories.NotificationRepository;
import hexagonal.modular.domain.notifications.web.dtos.NotificationResponse;
import hexagonal.modular.shared.usecases.IUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NotificationsAllUseCase implements IUseCase<Pageable, Page<NotificationResponse>> {
    private final NotificationRepository repository;

    public NotificationsAllUseCase(NotificationRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves all notifications from the repository and converts them to DTOs.
     *
     * @return a list of NotificationDTOs representing all notifications.
     */
    @Override
    public Page<NotificationResponse> execute(Pageable pageable) {
        var notifications = getAllNotifications(pageable);
        return notifications.map(NotificationResponse::new);
    }

    private Page<NotificationDocument> getAllNotifications(Pageable pageable) {
        return repository.findAllByOrderByCreatedAtDesc(pageable);
    }
}
