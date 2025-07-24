package hexagonal.modular.domain.notifications.web.controllers;

import hexagonal.modular.domain.notifications.core.usecases.NotificationMarkAsReadUseCase;
import hexagonal.modular.domain.notifications.core.usecases.NotificationMarkAsUnreadUseCase;
import hexagonal.modular.domain.notifications.core.usecases.NotificationsAllUseCase;
import hexagonal.modular.domain.notifications.core.usecases.NotificationsByReadStatusUseCase;
import hexagonal.modular.domain.notifications.web.dtos.NotificationResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationMarkAsReadUseCase markAsReadUseCase;
    private final NotificationMarkAsUnreadUseCase markAsUnreadUseCase;
    private final NotificationsByReadStatusUseCase readStatusUseCase;
    private final NotificationsAllUseCase notificationsAllUseCase;

    public NotificationController(NotificationMarkAsReadUseCase markAsReadUseCase, NotificationMarkAsUnreadUseCase markAsUnreadUseCase, NotificationsByReadStatusUseCase readStatusUseCase, NotificationsAllUseCase notificationsAllUseCase) {
        this.markAsReadUseCase = markAsReadUseCase;
        this.markAsUnreadUseCase = markAsUnreadUseCase;
        this.readStatusUseCase = readStatusUseCase;
        this.notificationsAllUseCase = notificationsAllUseCase;
    }

    @PatchMapping("/{id}/mark-as-read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markAsRead(@PathVariable String id) {
        markAsReadUseCase.execute(id);
    }

    @PatchMapping("/{id}/mark-as-unread")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markAsUnread(@PathVariable String id) {
        markAsUnreadUseCase.execute(id);
    }

    @GetMapping("/read")
    @ResponseStatus(HttpStatus.OK)
    public List<NotificationResponse> getNotificationsRead() {
        return readStatusUseCase.execute(true);
    }

    @GetMapping("/unread")
    @ResponseStatus(HttpStatus.OK)
    public List<NotificationResponse> getNotificationsUnread() {
        return readStatusUseCase.execute(false);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<NotificationResponse>> ListAllNotifications(@ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        var response = notificationsAllUseCase.execute(pageable);
        return ResponseEntity.ok(response);
    }
}
