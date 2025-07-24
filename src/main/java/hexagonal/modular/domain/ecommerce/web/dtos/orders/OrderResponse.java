package hexagonal.modular.domain.ecommerce.web.dtos.orders;

import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.OrderDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.enums.OrderStatusEnum;

import java.math.BigDecimal;
import java.util.Map;

public record OrderResponse(String id, String customerId, BigDecimal totalAmount, Map<String, Object> details,
                            OrderStatusEnum status) {

    public OrderResponse(OrderDocument order) {
        this(order.getId(), order.getCustomerId(), order.getTotalAmount(), order.getDetails(), order.getStatus());
    }
}