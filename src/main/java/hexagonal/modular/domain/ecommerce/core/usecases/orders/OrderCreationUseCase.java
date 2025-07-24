package hexagonal.modular.domain.ecommerce.core.usecases.orders;

import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.CartDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.OrderDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.enums.CartStatusEnum;
import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.CartRepository;
import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.OrderRepository;
import hexagonal.modular.domain.ecommerce.web.dtos.orders.CreateOrderRequest;
import hexagonal.modular.shared.exceptions.BusinessRuleException;
import hexagonal.modular.shared.usecases.IUseCase;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
public class OrderCreationUseCase implements IUseCase<CreateOrderRequest, String> {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public OrderCreationUseCase(OrderRepository orderRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    /**
     * Executes the use case to create an order based on the provided request.
     *
     * @param request The request containing order creation details.
     * @return The ID of the created order.
     */
    @Override
    @Transactional
    public String execute(CreateOrderRequest request) {

        var order = prepareData(request);
        orderRepository.save(order);
        return order.getId();
    }

    private OrderDocument prepareData(CreateOrderRequest request) {
        var order = OrderDocument.buildOrderDocument(request);

        var cart = cartRepository.findById(request.cartId()).orElseThrow(() -> new EntityNotFoundException("Cart not found with ID: " + request.cartId()));
        checkCartStatus(cart);
        updateCartStatusToFinished(cart);

        order.setCustomerId(cart.getConsumerId());
        order.setTotalAmount(cart.getTotalAmount());
        // set details data
        var cartDetails = new HashMap<String, Object>();
        cartDetails.put("cart", cart);
        order.setDetails(cartDetails);

        return order;
    }

    private void updateCartStatusToFinished(CartDocument cart) {
        cart.setStatus(CartStatusEnum.FINISHED);
        cartRepository.save(cart);
    }

    private void checkCartStatus(CartDocument cart) {
        if (!cart.getStatus().equals(CartStatusEnum.CHECKOUT)) {
            throw new BusinessRuleException("Cart is not in a valid state for order creation. Current Status: " + cart.getStatus());
        }
    }
}
