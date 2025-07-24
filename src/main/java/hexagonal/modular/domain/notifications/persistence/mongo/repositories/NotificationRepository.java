package hexagonal.modular.domain.notifications.persistence.mongo.repositories;

import hexagonal.modular.domain.notifications.persistence.mongo.documents.NotificationDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<NotificationDocument, String> {
    Page<NotificationDocument> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<NotificationDocument> findByRead(boolean read);
}
