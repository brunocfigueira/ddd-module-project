package hexagonal.modular.domain.sales.persitence.postgre.entities;

import hexagonal.modular.domain.sales.dtos.ConfirmSaleRequest;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales", uniqueConstraints = {@UniqueConstraint(columnNames = "order_id")})
public class SaleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 36)
    private String orderId;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public SaleEntity() {

    }

    public static SaleEntity buildSale(ConfirmSaleRequest request) {
        var sale = new SaleEntity();
        sale.setOrderId(request.order().getId());
        sale.setTotalAmount(request.order().getTotalAmount());
        sale.setCreatedAt(LocalDateTime.now());
        return sale;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

}
