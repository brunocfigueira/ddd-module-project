package hexagonal.modular.domain.ecommerce.core.usecases.products;

import hexagonal.modular.domain.ecommerce.events.ProductEvent;
import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.ProductDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.ProductRepository;
import hexagonal.modular.domain.ecommerce.web.dtos.products.CreateProductRequest;
import hexagonal.modular.shared.enums.EventTypeEnum;
import hexagonal.modular.shared.usecases.IUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductCreationUseCase implements IUseCase<CreateProductRequest, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCreationUseCase.class);
    private final ApplicationEventPublisher eventListener;
    private final ProductRepository repository;

    public ProductCreationUseCase(ProductRepository repository, ApplicationEventPublisher eventListener) {
        this.repository = repository;
        this.eventListener = eventListener;
    }

    /**
     * Executes the use case to create a product based on the provided request.
     *
     * @param request The request containing product creation details.
     * @return The ID of the created product.
     */
    @Override
    @Transactional
    public String execute(CreateProductRequest request) {
        LOGGER.info("Starting product creation: {}", request.name());

        var document = ProductDocument.buildProductDocument(request);
        repository.save(document);

        LOGGER.info("Product created successfully: {}", document.getId());
        publishEvent(document);
        return document.getId();
    }

    private void publishEvent(ProductDocument document) {
        LOGGER.info("Publishing product creation event with ID: {}", document.getId());
        eventListener.publishEvent(new ProductEvent(document, EventTypeEnum.RECORD_CREATION_NOTIFICATION));
    }
}
