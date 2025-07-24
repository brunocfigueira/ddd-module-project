package hexagonal.modular.domain.sales.listeners;

import hexagonal.modular.domain.ecommerce.events.SaleEvent;
import hexagonal.modular.domain.sales.core.usecases.ConfirmationSaleUseCase;
import hexagonal.modular.domain.sales.dtos.ConfirmSaleRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Async
@Component
public class SaleListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaleListener.class);

    private final ConfirmationSaleUseCase confirmationSaleUseCase;

    public SaleListener(ConfirmationSaleUseCase confirmationSaleUseCase) {
        this.confirmationSaleUseCase = confirmationSaleUseCase;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @ApplicationModuleListener
    public void onSaleEvent(SaleEvent event) {
        try {
            LOGGER.info("Received sale {} event for order ID: {}", event.type().name(), event.order().getId());
            confirmationSaleUseCase.execute(new ConfirmSaleRequest(event.order()));

        } catch (Exception ex) {
            LOGGER.error("Failed to process sale event for order ID: {}", event.order().getId(), ex);
            // TODO: Considere implementar retry logic ou dead letter queue
        }
    }
}
