package hexagonal.modular.domain.ecommerce.persistence.mongo.documents;

import hexagonal.modular.domain.ecommerce.persistence.mongo.enums.OrderStatusEnum;
import hexagonal.modular.domain.ecommerce.web.dtos.orders.CreateOrderRequest;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.Map;

@Document(collection = "orders")
public class OrderDocument {
    @Id
    private String id;

    @Indexed(name = "idx_customer")
    @Field("customerId")
    private String customerId;

    @Indexed(name = "idx_cart")
    @Field("cartId")
    private String cartId;

    @Field("totalAmount")
    private BigDecimal totalAmount;

    @Field("paymentReferenceKey")
    private String paymentReferenceKey;

    @Field("paymentMethodId")
    private String paymentMethodId;

    @Field("shippingAddressId")
    private String shippingAddressId;

    @Field("billingAddressId")
    private String billingAddressId;

    @Field("details")
    private Map<String, Object> details;

    @Field("status")
    private OrderStatusEnum status;

    public OrderDocument() {
    }

    public static OrderDocument buildOrderDocument(CreateOrderRequest request) {
        var order = new OrderDocument();
        order.setCartId(request.cartId());
        order.setPaymentMethodId(request.paymentMethodId());
        order.setShippingAddressId(request.shippingAddressId());
        order.setBillingAddressId(request.billingAddressId());
        order.setStatus(OrderStatusEnum.CREATED); // Default status
        return order;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentReferenceKey() {
        return paymentReferenceKey;
    }

    public void setPaymentReferenceKey(String paymentReferenceKey) {
        this.paymentReferenceKey = paymentReferenceKey;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getShippingAddressId() {
        return shippingAddressId;
    }

    public void setShippingAddressId(String shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }

    public String getBillingAddressId() {
        return billingAddressId;
    }

    public void setBillingAddressId(String billingAddressId) {
        this.billingAddressId = billingAddressId;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }
}
