package hexagonal.modular.domain.ecommerce.core.usecases.products;

import hexagonal.modular.domain.ecommerce.events.ProductEvent;
import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.ProductDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.ProductRepository;
import hexagonal.modular.domain.ecommerce.web.dtos.products.CreateProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductCreationUseCaseTest {

    @Mock
    private ApplicationEventPublisher eventListener;

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductCreationUseCase useCase;

    @Test
    @DisplayName("Should create product successfully")
    void shouldCreateProductSuccessfully() {
        var request = new CreateProductRequest("Test Product", BigDecimal.TEN);

        when(repository.save(any(ProductDocument.class))).thenReturn(any(ProductDocument.class));

        useCase.execute(request);

        verify(repository).save(any(ProductDocument.class));
        verify(eventListener).publishEvent(any(ProductEvent.class));
    }
}
