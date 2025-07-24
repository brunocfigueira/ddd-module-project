package hexagonal.modular.shared.enums;

public enum EventTypeEnum {
    PAYMENT_REQUEST("Payment Request"),
    CONFIRMATION_SALE("Confirmation Sale"),
    INTERNAL_NOTIFICATION("Internal Notification"),
    EXTERNAL_NOTIFICATION("External Notification"),
    RECORD_CREATION_NOTIFICATION("Record Creation Notification"),
    RECORD_UPDATE_NOTIFICATION("Record Update Notification"),
    RECORD_DELETE_NOTIFICATION("Record Delete Notification");

    private final String value;

    EventTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
