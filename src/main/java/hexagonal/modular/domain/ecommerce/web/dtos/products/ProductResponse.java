package hexagonal.modular.domain.ecommerce.web.dtos.products;

import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.ProductDocument;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(String id, String name, BigDecimal price, LocalDateTime createdAt,
                              LocalDateTime updatedAt,
                              boolean active) {
    public ProductResponse(ProductDocument document) {
        this(document.getId(), document.getName(), document.getPrice(), document.getCreatedAt(), document.getUpdatedAt(), document.isActive());
    }
}
