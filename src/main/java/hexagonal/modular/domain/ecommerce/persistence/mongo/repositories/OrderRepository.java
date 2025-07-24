package hexagonal.modular.domain.ecommerce.persistence.mongo.repositories;

import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.OrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderDocument, String> {
}
