package hexagonal.modular.domain.payments.dtos;

import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.OrderDocument;

public record PaymentRequest(OrderDocument order) {
}
