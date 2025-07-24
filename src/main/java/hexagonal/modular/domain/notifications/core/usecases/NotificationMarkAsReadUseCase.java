package hexagonal.modular.domain.notifications.core.usecases;

import hexagonal.modular.domain.notifications.persistence.mongo.repositories.NotificationRepository;
import hexagonal.modular.shared.usecases.IUnitUseCase;
import hexagonal.modular.shared.utils.IValidatorUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationMarkAsReadUseCase implements IUnitUseCase<String> {
    private final NotificationRepository repository;

    public NotificationMarkAsReadUseCase(NotificationRepository repository) {
        this.repository = repository;
    }

    /**
     * Marks a notification as read by its ID.
     *
     * @param id the ID of the notification to mark as read
     * @throws EntityNotFoundException if the notification with the given ID does not exist
     */
    @Override
    @Transactional
    public void execute(String id) {
        IValidatorUtil.requireNonNull(id, "Notification ID cannot be null");

        var document = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Notification not found"));
        document.setRead(true);
        repository.save(document);
    }
}
