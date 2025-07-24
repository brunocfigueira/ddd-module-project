package hexagonal.modular.domain.notifications.web.dtos;

import hexagonal.modular.domain.notifications.persistence.mongo.documents.NotificationDocument;

import java.util.Map;

public record NotificationResponse(String id,
                                   String sender,
                                   String recipient,
                                   String referenceCode,
                                   String title,
                                   String content,
                                   String type,
                                   Map<String, Object> metadata,
                                   boolean read,
                                   String createdAt,
                                   String expiresAt) {

    public NotificationResponse(NotificationDocument document) {
        this(document.getId(),
                document.getSender(),
                document.getRecipient(),
                document.getReferenceValue(),
                document.getTitle(),
                document.getMessage(),
                document.getType().name(),
                document.getContent(),
                document.isRead(),
                document.getCreatedAt().toString(),
                document.getUpdatedAt() != null ? document.getUpdatedAt().toString() : null);
    }
}
