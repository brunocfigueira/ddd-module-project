package hexagonal.modular.domain.ecommerce.events;

import com.mongodb.lang.NonNull;
import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.OrderDocument;
import hexagonal.modular.shared.enums.EventTypeEnum;

public record PaymentEvent(@NonNull OrderDocument order, @NonNull EventTypeEnum type) {
}
