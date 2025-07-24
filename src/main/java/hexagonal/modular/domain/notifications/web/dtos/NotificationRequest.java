package hexagonal.modular.domain.notifications.web.dtos;

import hexagonal.modular.domain.notifications.persistence.mongo.enums.NotificationTypeEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

/**
 * Represents a request to create a notification.
 * This record is used to encapsulate the data required for creating a notification.
 */

public record NotificationRequest(
        @NotEmpty String sender,
        @NotEmpty String recipient,
        @NotEmpty String referenceValue,
        @NotNull NotificationTypeEnum type,
        @NotEmpty String title,
        @NotEmpty String message,
        Map<String, Object> content
) {
}
