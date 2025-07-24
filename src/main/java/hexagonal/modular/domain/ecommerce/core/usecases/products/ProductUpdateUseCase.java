package hexagonal.modular.domain.ecommerce.core.usecases.products;

import hexagonal.modular.domain.ecommerce.events.ProductEvent;
import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.ProductDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.ProductRepository;
import hexagonal.modular.domain.ecommerce.web.dtos.products.UpdateProductRequest;
import hexagonal.modular.shared.enums.EventTypeEnum;
import hexagonal.modular.shared.usecases.IUnitUseCase;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductUpdateUseCase implements IUnitUseCase<UpdateProductRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductUpdateUseCase.class);
    private final ApplicationEventPublisher eventListener;
    private final ProductRepository repository;

    public ProductUpdateUseCase(ApplicationEventPublisher eventListener, ProductRepository repository) {
        this.eventListener = eventListener;
        this.repository = repository;
    }

    /**
     * Executa o caso de uso para atualizar um produto com base na solicitação fornecida.
     *
     * @param request o parâmetro de entrada para a execução, do tipo {@code I}
     */
    @Override
    @Transactional
    public void execute(UpdateProductRequest request) {

        repository.findById(request.id()).ifPresentOrElse(product -> {
            LOGGER.info("Starting product update: {}", request.name());
            product.setName(request.name());
            product.setPrice(request.price());
            product.setActive(request.active());
            repository.save(product);

            LOGGER.info("Product updated successfully: {}", product.getId());

            publishEvent(product);
        }, () -> {
            throw new EntityNotFoundException("Product with ID: " + request.id() + " not found.");
        });
    }

    private void publishEvent(ProductDocument document) {
        LOGGER.info("Publishing product update event with ID: {}", document.getId());
        eventListener.publishEvent(new ProductEvent(document, EventTypeEnum.RECORD_DELETE_NOTIFICATION));
    }
}
