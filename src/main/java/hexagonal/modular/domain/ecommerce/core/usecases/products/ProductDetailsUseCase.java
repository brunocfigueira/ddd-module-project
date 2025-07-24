package hexagonal.modular.domain.ecommerce.core.usecases.products;

import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.ProductRepository;
import hexagonal.modular.domain.ecommerce.web.dtos.products.ProductResponse;
import hexagonal.modular.shared.usecases.IUseCase;
import hexagonal.modular.shared.utils.IValidatorUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ProductDetailsUseCase implements IUseCase<String, ProductResponse> {
    private final ProductRepository repository;

    public ProductDetailsUseCase(ProductRepository repository) {
        this.repository = repository;
    }

    /**
     * Executes the use case to read a product based on the provided ID.
     *
     * @param id The ID of the product to be read.
     * @return A ProductResponse containing the product details.
     */
    @Override
    @Cacheable(value = "products", key = "#id")
    public ProductResponse execute(String id) {
        IValidatorUtil.requireNotBlank(id, "Product ID cannot be null");
        var product = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product with ID: " + id + " not found."));
        return new ProductResponse(product);
    }
}
