package hexagonal.modular.domain.ecommerce.core.rules;

import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.CartDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.CartItems;
import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.ProductRepository;
import hexagonal.modular.shared.exceptions.BusinessRuleException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class CartRule implements ICartRule {
    private final ProductRepository productRepository;

    public CartRule(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Aplica as regras de negócio no carrinho de compras.
     *
     * @param cart
     */
    @Override
    public void apply(CartDocument cart) {
        // TODO: Implementar regras de negócio - verificar se o consumidor existe, etc.
        checkBasicRules(cart.getItems());
        calculateTotalPriceOfItems(cart.getItems(), true);
        calculateCartTotalAmount(cart);
    }

    private void applyCalculation(CartItems item, BigDecimal productPrice, boolean withDiscount) {
        if (productPrice == null || productPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("Product price must be greater than zero.");
        }

        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            throw new BusinessRuleException("Quantity must be greater than zero.");
        }
        item.setDiscount(BigDecimal.ZERO);
        if (withDiscount) {
            applyDiscount(item);
        }
        item.setUnitPrice(productPrice);
        BigDecimal subtotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        BigDecimal discountAmount = subtotal.multiply(item.getDiscount());
        item.setTotalPrice(subtotal.subtract(discountAmount));
    }

    private void applyDiscount(CartItems item) {
        if (item.getQuantity() < 4) {
            item.setDiscount(BigDecimal.ZERO);
        } else if (item.getQuantity() < 10) {
            item.setDiscount(BigDecimal.valueOf(0.10));
        } else if (item.getQuantity() <= 20) {
            item.setDiscount(BigDecimal.valueOf(0.20));
        } else {
            throw new BusinessRuleException("It is not possible to sell more than 20 identical products.");
        }
    }

    private void calculateTotalPriceOfItems(List<CartItems> items, boolean withDiscount) {
        items.forEach(item -> {
            var product = productRepository.findById(item.getProductId()).orElseThrow(() -> new EntityNotFoundException("Product with id: '" + item.getProductId() + "' not found"));
            applyCalculation(item, product.getPrice(), withDiscount);
        });
    }

    private void checkBasicRules(List<CartItems> items) {
        checkDuplicateItems(items);
        checkExistenceAndStatusProduct(items);
    }

    private void checkExistenceAndStatusProduct(List<CartItems> items) {
        for (CartItems item : items) {
            productRepository.findById(item.getProductId()).ifPresentOrElse(product -> {
                if (!product.isActive()) {
                    throw new BusinessRuleException("Product with id: '" + item.getProductId() + "' is not active");
                }
            }, () -> {
                throw new EntityNotFoundException("Product with id: '" + item.getProductId() + "' not found");
            });
        }
    }

    private void checkDuplicateItems(List<CartItems> items) {
        Set<String> productIds = new HashSet<>();
        boolean hasDuplicates = items.stream().anyMatch(item -> !productIds.add(item.getProductId()));

        if (hasDuplicates) {
            throw new BusinessRuleException("Duplicate product ID detected in cart");
        }
    }

    private void calculateCartTotalAmount(CartDocument cart) {
        BigDecimal totalAmount = cart.getItems().stream().map(CartItems::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(totalAmount);
    }
}
