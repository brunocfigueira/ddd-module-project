package hexagonal.modular.domain.sales.core.usecases;

import hexagonal.modular.domain.sales.dtos.ConfirmSaleRequest;
import hexagonal.modular.domain.sales.persitence.postgre.entities.SaleEntity;
import hexagonal.modular.domain.sales.persitence.postgre.repositories.SaleRepository;
import hexagonal.modular.shared.usecases.IUnitUseCase;
import hexagonal.modular.shared.utils.IValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfirmationSaleUseCase implements IUnitUseCase<ConfirmSaleRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmationSaleUseCase.class);
    private final SaleRepository repository;

    public ConfirmationSaleUseCase(SaleRepository repository) {
        this.repository = repository;
    }

    /**
     * Executa a confirmação de uma venda.
     *
     * @param request o parâmetro de entrada para a execução, do tipo {@code I}
     */
    @Override
    @Transactional
    public void execute(ConfirmSaleRequest request) {
        IValidatorUtil.requireNonNull(request, "Request must not be null");
        IValidatorUtil.requireNonNull(request.order(), "Order must not be null");
        LOGGER.info("Starting sales confirmation for order with ID: {}", request.order().getId());
        var entity = SaleEntity.buildSale(request);
        repository.save(entity);
        LOGGER.info("Sales confirmation successfully: {}", entity.getId());
    }
}
