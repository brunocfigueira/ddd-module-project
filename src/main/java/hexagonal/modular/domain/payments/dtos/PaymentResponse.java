package hexagonal.modular.domain.payments.dtos;

import hexagonal.modular.domain.payments.enums.PaymentStatus;

public record PaymentResponse(PaymentStatus status,
                              String paymentReferenceKey,
                              String clientSecret) {
}
