package hexagonal.modular.domain.ecommerce.core.usecases.carts;

import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.CartRepository;
import hexagonal.modular.domain.ecommerce.web.dtos.carts.CartResponse;
import hexagonal.modular.shared.usecases.IUseCase;
import hexagonal.modular.shared.utils.IValidatorUtil;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CartDetailsUseCase implements IUseCase<String, CartResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartDetailsUseCase.class);
    private final CartRepository repository;

    public CartDetailsUseCase(CartRepository repository) {
        this.repository = repository;
    }

    /**
     * Método responsável por obter os detalhes de um carrinho de compras.
     *
     * @param id o parâmetro de entrada para a execução, do tipo {@code Long}
     * @return os detalhes do carrinho de compras
     */
    @Override
    @Cacheable(value = "carts", key = "#id")
    public CartResponse execute(String id) {
        IValidatorUtil.requireNonNull(id, "Cart ID cannot be null");

        LOGGER.info("Starting details for cart with id: {}", id);
        var cart = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        LOGGER.info("Cart details retrieved successfully: {}", id);

        return new CartResponse(cart);
    }
}
