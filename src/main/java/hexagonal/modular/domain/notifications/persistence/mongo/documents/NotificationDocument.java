package hexagonal.modular.domain.notifications.persistence.mongo.documents;

import hexagonal.modular.domain.notifications.persistence.mongo.enums.NotificationTypeEnum;
import hexagonal.modular.domain.notifications.web.dtos.NotificationRequest;
import jakarta.persistence.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "notifications")
public class NotificationDocument {

    @Id
    private String id;

    @Indexed(name = "idx_sender")
    @Field("sender")
    private String sender; // who is the sender

    @Indexed(name = "idx_recipient")
    @Field("recipient")
    private String recipient; // who is the recipient

    @Indexed(name = "idx_reference_code")
    @Field("reference_code")
    private String referenceValue; //any value to reference a user, an application, an indexer code, etc.

    @Indexed(name = "idx_type")
    @Field("type")
    private NotificationTypeEnum type;

    @Field("title")
    private String title;

    @Field("message")
    private String message;

    @Field("content")
    private Map<String, Object> content;

    @Field("read")
    private boolean read;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field(name = "updated_at")
    private LocalDateTime updatedAt;

    public NotificationDocument() {
    }

    public static NotificationDocument buildNotification(NotificationRequest request) {
        var document = new NotificationDocument();
        document.setSender(request.sender().toUpperCase());
        document.setRecipient(request.recipient().toUpperCase());
        document.setType(request.type());
        document.setReferenceValue(request.referenceValue());
        document.setTitle(request.title());
        document.setMessage(request.message());
        document.setContent(request.content());
        document.setRead(false);
        return document;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getReferenceValue() {
        return referenceValue;
    }

    public void setReferenceValue(String referenceValue) {
        this.referenceValue = referenceValue;
    }

    public NotificationTypeEnum getType() {
        return type;
    }

    public void setType(NotificationTypeEnum type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
