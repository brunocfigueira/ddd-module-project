package hexagonal.modular.domain.ecommerce.web.dtos.carts;

import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.CartDocument;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CartResponse(String id,
                           String consumerId,
                           List<CartItemsResponse> items,
                           BigDecimal totalAmount,
                           LocalDateTime updatedAt) {

    public CartResponse(CartDocument cart) {
        this(cart.getId(),
                cart.getConsumerId(),
                cart.getItems().stream()
                        .map(CartItemsResponse::new)
                        .toList(),
                cart.getTotalAmount(),
                cart.getUpdatedAt());
    }

}
