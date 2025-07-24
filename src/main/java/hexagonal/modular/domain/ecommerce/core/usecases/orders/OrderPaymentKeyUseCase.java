package hexagonal.modular.domain.ecommerce.core.usecases.orders;

import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.OrderRepository;
import hexagonal.modular.domain.ecommerce.web.dtos.orders.UpdatePaymentKeyRequest;
import hexagonal.modular.shared.usecases.IUnitUseCase;
import hexagonal.modular.shared.utils.IValidatorUtil;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderPaymentKeyUseCase implements IUnitUseCase<UpdatePaymentKeyRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderPaymentKeyUseCase.class);
    private final OrderRepository repository;

    public OrderPaymentKeyUseCase(OrderRepository repository) {
        this.repository = repository;
    }

    /**
     * Executa a atualização da chave de pagamento de um pedido.
     *
     * @param request o parâmetro de entrada para a execução, do tipo {@code UpdatePaymentKeyRequest}
     */
    @Override
    public void execute(UpdatePaymentKeyRequest request) {
        IValidatorUtil.requireNonNull(request, " Request must not be null");
        IValidatorUtil.requireNotBlank(request.orderId(), "Order ID must not be null or empty");
        IValidatorUtil.requireNotBlank(request.paymentReferenceKey(), "Payment reference key must not be null or empty");
        IValidatorUtil.requireNonNull(request.status(), "Order status must not be null");
        LOGGER.info("Updating payment key for order with ID: {}", request.orderId());
        repository.findById(request.orderId()).ifPresentOrElse(order -> {
            order.setStatus(request.status());
            order.setPaymentReferenceKey(request.paymentReferenceKey());
            repository.save(order);
            LOGGER.info("Payment key update completed successfully: {}", request.orderId());
        }, () -> {
            throw new EntityNotFoundException("Cart not found with ID: " + request.orderId());
        });
    }
}
