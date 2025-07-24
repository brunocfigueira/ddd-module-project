package hexagonal.modular.domain.ecommerce.core.usecases.products;

import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.ProductRepository;
import hexagonal.modular.domain.ecommerce.web.dtos.products.ProductResponse;
import hexagonal.modular.shared.usecases.IUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductSearchUseCase implements IUseCase<Pageable, Page<ProductResponse>> {
    private final ProductRepository repository;

    public ProductSearchUseCase(ProductRepository repository) {
        this.repository = repository;
    }

    /**
     * Executes the use case to search for products with pagination support.
     *
     * @param pageable o parâmetro de entrada para a execução, do tipo {@code I}
     * @return Page<ProductResponse>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> execute(Pageable pageable) {
        return repository.findAll(pageable).map(ProductResponse::new);
    }
}
