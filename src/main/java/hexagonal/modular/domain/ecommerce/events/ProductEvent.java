package hexagonal.modular.domain.ecommerce.events;

import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.ProductDocument;
import hexagonal.modular.shared.enums.EventTypeEnum;
import jakarta.validation.constraints.NotNull;

public record ProductEvent(@NotNull ProductDocument product,
                           @NotNull EventTypeEnum type) {

}
