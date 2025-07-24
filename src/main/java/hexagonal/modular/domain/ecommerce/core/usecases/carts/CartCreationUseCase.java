package hexagonal.modular.domain.ecommerce.core.usecases.carts;

import hexagonal.modular.domain.ecommerce.core.rules.ICartRule;
import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.CartDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.CartRepository;
import hexagonal.modular.domain.ecommerce.web.dtos.carts.CreateCartRequest;
import hexagonal.modular.shared.usecases.IUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartCreationUseCase implements IUseCase<CreateCartRequest, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CartCreationUseCase.class);
    private final ApplicationEventPublisher eventListener;

    private final CartRepository cartRepository;
    private final List<ICartRule> rules;

    public CartCreationUseCase(ApplicationEventPublisher eventListener, CartRepository cartRepository, List<ICartRule> rules) {
        this.eventListener = eventListener;

        this.cartRepository = cartRepository;
        this.rules = rules;
    }

    /**
     * Método responsável por criar um novo carrinho de compras.
     *
     * @param createCartRequest o parâmetro de entrada para a execução, do tipo {@code CartRequest}
     * @return o ID do carrinho criado
     */
    @Override
    public String execute(CreateCartRequest createCartRequest) {
        LOGGER.info("Starting to create the shopping cart for the consumer: {}", createCartRequest.consumerId());

        var cart = CartDocument.buildCartDocument(createCartRequest);
        rules.forEach(r -> r.apply(cart));

        cartRepository.save(cart);

        LOGGER.info("Cart created successfully: {}", cart.getConsumerId());
        return cart.getId();
    }
}
