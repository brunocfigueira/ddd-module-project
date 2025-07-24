package hexagonal.modular.domain.sales.dtos;

import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.OrderDocument;
import jakarta.validation.constraints.NotNull;

public record ConfirmSaleRequest(@NotNull OrderDocument order) {
}
