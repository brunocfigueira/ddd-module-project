package hexagonal.modular.domain.ecommerce.core.rules;

import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.CartDocument;

public interface ICartRule {
    void apply(CartDocument document);
}
