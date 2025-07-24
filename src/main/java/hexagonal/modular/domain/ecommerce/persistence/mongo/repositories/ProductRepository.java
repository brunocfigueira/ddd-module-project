package hexagonal.modular.domain.ecommerce.persistence.mongo.repositories;

import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.ProductDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<ProductDocument, String> {
}
