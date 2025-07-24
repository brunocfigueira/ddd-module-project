package hexagonal.modular.domain.ecommerce.persistence.mongo.repositories;

import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.CartDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<CartDocument, String> {
}
