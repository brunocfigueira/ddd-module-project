package hexagonal.modular.domain.notifications.core.usecases;

import hexagonal.modular.domain.notifications.persistence.mongo.repositories.NotificationRepository;
import hexagonal.modular.shared.usecases.IUnitUseCase;
import hexagonal.modular.shared.utils.IValidatorUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationMarkAsUnreadUseCase implements IUnitUseCase<String> {
    private final NotificationRepository repository;

    public NotificationMarkAsUnreadUseCase(NotificationRepository repository) {
        this.repository = repository;
    }

    /**
     * Marks a notification as unread by its ID.
     *
     * @param id the ID of the notification to mark as unread
     * @throws EntityNotFoundException if the notification with the given ID does not exist
     */
    @Override
    @Transactional
    public void execute(String id) {
        IValidatorUtil.requireNotBlank(id, "Notification ID cannot be null");

        var document = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Notification not found"));
        document.setRead(false);
        repository.save(document);
    }
}
