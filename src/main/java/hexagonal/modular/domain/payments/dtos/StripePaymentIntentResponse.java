package hexagonal.modular.domain.payments.dtos;

public record StripePaymentIntentResponse(String id, String client_secret) {
}
