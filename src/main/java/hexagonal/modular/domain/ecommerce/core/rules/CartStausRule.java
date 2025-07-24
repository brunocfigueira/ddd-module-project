package hexagonal.modular.domain.ecommerce.core.rules;

import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.CartDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.enums.CartStatusEnum;
import hexagonal.modular.shared.exceptions.BusinessRuleException;
import org.springframework.stereotype.Component;

@Component
public class CartStausRule implements ICartRule {
    /**
     * Regra de negócio para verificar o status do carrinho.
     * Garante que o carrinho esteja aberto antes de permitir alterações.
     */
    @Override
    public void apply(CartDocument cart) {
        checkCartsStatus(cart);
    }

    private void checkCartsStatus(CartDocument cart) {
        if (!cart.getStatus().equals(CartStatusEnum.OPENED)) {
            throw new BusinessRuleException("The cart cannot be changed. Status: " + cart.getStatus().name());
        }
    }
}
