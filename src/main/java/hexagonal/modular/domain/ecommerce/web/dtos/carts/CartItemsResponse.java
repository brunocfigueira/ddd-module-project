package hexagonal.modular.domain.ecommerce.web.dtos.carts;

import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.CartItems;

import java.math.BigDecimal;

public record CartItemsResponse(String productId,
                                Integer quantity,
                                BigDecimal unitPrice,
                                BigDecimal discount,
                                BigDecimal totalPrice) {

    public CartItemsResponse(CartItems items) {
        this(items.getProductId(),
                items.getQuantity(),
                items.getUnitPrice(),
                items.getDiscount(),
                items.getTotalPrice());
    }
}
