package hexagonal.modular.domain.notifications.core.usecases;

import hexagonal.modular.domain.notifications.persistence.mongo.repositories.NotificationRepository;
import hexagonal.modular.domain.notifications.web.dtos.NotificationResponse;
import hexagonal.modular.shared.usecases.IUseCase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationsByReadStatusUseCase implements IUseCase<Boolean, List<NotificationResponse>> {
    private final NotificationRepository repository;

    public NotificationsByReadStatusUseCase(NotificationRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves notifications by their statusCode.
     *
     * @param read the statusCode of the notifications to retrieve
     * @return a list of NotificationDTO objects representing the notifications with the specified statusCode
     */
    @Override
    public List<NotificationResponse> execute(Boolean read) {
        return repository.findByRead(read).stream().map(NotificationResponse::new).toList();
    }
}
