package hexagonal.modular.domain.ecommerce.core.usecases.orders;

import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.OrderRepository;
import hexagonal.modular.domain.ecommerce.web.dtos.orders.OrderResponse;
import hexagonal.modular.shared.usecases.IUseCase;
import hexagonal.modular.shared.utils.IValidatorUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailsUseCase implements IUseCase<String, OrderResponse> {
    private final OrderRepository repository;

    public OrderDetailsUseCase(OrderRepository repository) {
        this.repository = repository;
    }

    /**
     * Executes the use case to read an order based on the provided ID.
     *
     * @param id The ID of the order to be read.
     * @return An OrderResponse containing the order details.
     * @throws EntityNotFoundException if the order with the given ID does not exist.
     */
    @Override
    @Cacheable(value = "orders", key = "#id")
    public OrderResponse execute(String id) {
        IValidatorUtil.requireNotBlank(id, "Order ID cannot be null or empty");
        var order = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order with ID: " + id + " not found."));
        return new OrderResponse(order);
    }
}
