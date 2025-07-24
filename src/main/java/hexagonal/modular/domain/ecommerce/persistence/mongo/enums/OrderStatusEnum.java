package hexagonal.modular.domain.ecommerce.persistence.mongo.enums;

public enum OrderStatusEnum {
    CREATED,
    PENDING,
    APPROVED,
    SHIPPED,
    DELIVERED,
    CANCELED;
}
