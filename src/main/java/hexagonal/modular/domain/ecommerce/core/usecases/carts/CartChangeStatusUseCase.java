package hexagonal.modular.domain.ecommerce.core.usecases.carts;

import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.CartDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.enums.CartStatusEnum;
import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.CartRepository;
import hexagonal.modular.shared.exceptions.BusinessRuleException;
import hexagonal.modular.shared.usecases.IUnitUseCase;
import hexagonal.modular.shared.utils.IValidatorUtil;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CartChangeStatusUseCase implements IUnitUseCase<CartChangeStatusUseCase.Input> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CartChangeStatusUseCase.class);
    private final CartRepository cartRepository;

    public CartChangeStatusUseCase(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    /**
     * @param input o parâmetro de entrada para a execução, do tipo {@code I}
     */
    @Override
    public void execute(Input input) {
        IValidatorUtil.requireNonNull(input, "Input must not be null");

        cartRepository.findById(input.cartId()).ifPresentOrElse(
                cart -> {
                    checkCartStatus(cart);
                    cart.setStatus(input.status());
                    cartRepository.save(cart);

                    LOGGER.info("Cart status updated successfully: {}", input.cartId());

                }, () -> {
                    throw new EntityNotFoundException("Cart with ID " + input.cartId + " not found");
                });
    }

    private void checkCartStatus(CartDocument cart) {
        if (cart.getStatus().equals(CartStatusEnum.FINISHED) || cart.getStatus().equals(CartStatusEnum.CANCELED)) {
            LOGGER.warn("Cart with id {} cannot be opened. Status: {}", cart.getId(), cart.getStatus());
            throw new BusinessRuleException("The cart cannot be changed. Status: " + cart.getStatus().name());
        }
    }


    public record Input(String cartId, CartStatusEnum status) {
        public Input {
            IValidatorUtil.requireNotBlank(cartId, "Cart ID must not be null or empty");
            IValidatorUtil.requireNonNull(status, "Cart status must not be null");
        }
    }
}
