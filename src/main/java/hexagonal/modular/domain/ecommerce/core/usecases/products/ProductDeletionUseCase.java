package hexagonal.modular.domain.ecommerce.core.usecases.products;

import hexagonal.modular.domain.ecommerce.events.ProductEvent;
import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.ProductDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.ProductRepository;
import hexagonal.modular.shared.enums.EventTypeEnum;
import hexagonal.modular.shared.usecases.IUnitUseCase;
import hexagonal.modular.shared.utils.IValidatorUtil;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductDeletionUseCase implements IUnitUseCase<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDeletionUseCase.class);
    private final ApplicationEventPublisher eventListener;
    private final ProductRepository repository;

    public ProductDeletionUseCase(ApplicationEventPublisher eventListener, ProductRepository repository) {
        this.eventListener = eventListener;
        this.repository = repository;
    }

    /**
     * Executa o caso de uso para excluir um produto com base no ID fornecido.
     *
     * @param id o parâmetro de entrada para a execução, do tipo {@code I}
     */
    @Override
    @Transactional
    public void execute(String id) {
        IValidatorUtil.requireNotBlank(id, "Product ID cannot be null");

        repository.findById(id).ifPresentOrElse(product -> {
            LOGGER.info("Starting product delete: {}", id);
            repository.delete(product);

            LOGGER.info("Product deleted successfully: {}", id);

            publishEvent(product);
            
        }, () -> {
            throw new EntityNotFoundException("Product with ID: " + id + " not found.");
        });
    }

    private void publishEvent(ProductDocument document) {
        LOGGER.info("Publishing product deletion event with ID: {}", document.getId());
        eventListener.publishEvent(new ProductEvent(document, EventTypeEnum.RECORD_DELETE_NOTIFICATION));
    }
}
