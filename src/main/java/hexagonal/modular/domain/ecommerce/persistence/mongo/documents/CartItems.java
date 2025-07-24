package hexagonal.modular.domain.ecommerce.persistence.mongo.documents;

import hexagonal.modular.domain.ecommerce.web.dtos.carts.CartItemsRequest;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

public class CartItems {
    @Field("product_id")
    private String productId;
    @Field("quantity")
    private Integer quantity;
    @Field("unit_price")
    private BigDecimal unitPrice;
    @Field("discount")
    private BigDecimal discount;
    @Field("total_price")
    private BigDecimal totalPrice;

    public CartItems() {
    }

    public CartItems(String productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static CartItems buildCartItem(CartItemsRequest request) {
        return new CartItems(request.productId(), request.quantity());
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
