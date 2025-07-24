package hexagonal.modular.domain.ecommerce.persistence.mongo.documents;

import hexagonal.modular.domain.ecommerce.persistence.mongo.enums.CartStatusEnum;
import hexagonal.modular.domain.ecommerce.web.dtos.carts.CartItemsRequest;
import hexagonal.modular.domain.ecommerce.web.dtos.carts.CreateCartRequest;
import jakarta.persistence.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "carts")
public class CartDocument {
    @Id
    private String id;

    @Indexed(name = "idx_consumer_id")
    @Field(name = "consumer_id")
    private String consumerId;

    @Field(name = "items")
    private List<CartItems> items;

    @Field(name = "status")
    private CartStatusEnum status;

    @Field(name = "total_amount")
    private BigDecimal totalAmount;

    @CreatedDate
    @Field(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field(name = "updated_at")
    private LocalDateTime updatedAt;

    public CartDocument() {
    }

    public static CartDocument buildCartDocument(CreateCartRequest request) {
        var document = new CartDocument();
        document.setConsumerId(request.consumerId());
        document.setItems(buildCartItems(request.items()));
        document.setStatus(CartStatusEnum.OPENED);
        return document;
    }

    public static List<CartItems> buildCartItems(List<CartItemsRequest> items) {
        var listItems = new ArrayList<CartItems>();
        items.forEach(itemRequest -> listItems.add(CartItems.buildCartItem(itemRequest)));
        return listItems;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public List<CartItems> getItems() {
        return items;
    }

    public void setItems(List<CartItems> items) {
        this.items = items;
    }

    public CartStatusEnum getStatus() {
        return status;
    }

    public void setStatus(CartStatusEnum status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
