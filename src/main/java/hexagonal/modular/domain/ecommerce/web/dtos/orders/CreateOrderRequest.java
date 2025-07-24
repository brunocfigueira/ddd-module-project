package hexagonal.modular.domain.ecommerce.web.dtos.orders;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequest(
        @NotBlank
        String cartId, // The ID of the cart to create the order from
        @NotBlank
        String paymentMethodId, // The ID of the payment method
        @NotBlank
        String shippingAddressId, // The ID of the shipping address
        @NotBlank
        String billingAddressId // The ID of the billing address
) {
}
