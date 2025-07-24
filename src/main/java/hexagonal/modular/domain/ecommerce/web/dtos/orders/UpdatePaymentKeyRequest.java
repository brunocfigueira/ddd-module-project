package hexagonal.modular.domain.ecommerce.web.dtos.orders;

import hexagonal.modular.domain.ecommerce.persistence.mongo.enums.OrderStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdatePaymentKeyRequest(@NotBlank String orderId,
                                      @NotBlank String paymentReferenceKey,
                                      @NotNull OrderStatusEnum status) {

}
