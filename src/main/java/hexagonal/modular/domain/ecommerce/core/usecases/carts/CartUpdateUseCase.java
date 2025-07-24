package hexagonal.modular.domain.ecommerce.core.usecases.carts;

import hexagonal.modular.domain.ecommerce.core.rules.ICartRule;
import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.CartDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.CartRepository;
import hexagonal.modular.domain.ecommerce.web.dtos.carts.UpdateCartRequest;
import hexagonal.modular.shared.usecases.IUnitUseCase;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartUpdateUseCase implements IUnitUseCase<UpdateCartRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartUpdateUseCase.class);
    private final ApplicationEventPublisher eventListener;
    private final CartRepository repository;
    private final List<ICartRule> rules;

    public CartUpdateUseCase(ApplicationEventPublisher eventListener, CartRepository repository, List<ICartRule> rules) {
        this.eventListener = eventListener;
        this.repository = repository;
        this.rules = rules;
    }

    /**
     * Método responsável por atualizar um carrinho de compras existente.
     *
     * @param request o parâmetro de entrada para a execução, do tipo {@code I}
     */
    @Override
    @Transactional
    public void execute(UpdateCartRequest request) {
        LOGGER.info("Starting to update the shopping cart with id: {}", request.id());
        repository.findById(request.id()).ifPresentOrElse(cart -> {
            cart.getItems().clear();
            cart.setItems(CartDocument.buildCartItems(request.items()));
            rules.forEach(rule -> rule.apply(cart));
            repository.save(cart);

            LOGGER.info("Cart updated successfully: {}", cart.getId());

        }, () -> {
            throw new EntityNotFoundException("Cart with ID " + request.id() + " not found");
        });
    }
}
