package hexagonal.modular.domain.notifications.core.usecases;

import hexagonal.modular.domain.notifications.persistence.mongo.documents.NotificationDocument;
import hexagonal.modular.domain.notifications.persistence.mongo.repositories.NotificationRepository;
import hexagonal.modular.domain.notifications.web.dtos.NotificationRequest;
import hexagonal.modular.shared.usecases.IUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationCreationUseCase implements IUseCase<NotificationRequest, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationCreationUseCase.class);
    private final NotificationRepository repository;

    public NotificationCreationUseCase(NotificationRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates a new system notification based on the provided request.
     *
     * @param request the notification request containing details for the new notification
     * @return the ID of the created notification
     */
    @Override
    @Transactional
    public String execute(NotificationRequest request) {
        LOGGER.info("Starting notification creation: {}", request.type().name());
        var document = NotificationDocument.buildNotification(request);
        repository.save(document);
        LOGGER.info("Notification created successfully: {}", document.getId());
        return document.getId();
    }
}
