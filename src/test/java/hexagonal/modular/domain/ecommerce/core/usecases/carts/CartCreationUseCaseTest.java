package hexagonal.modular.domain.ecommerce.core.usecases.carts;

import hexagonal.modular.domain.ecommerce.core.rules.ICartRule;
import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.CartDocument;
import hexagonal.modular.domain.ecommerce.persistence.mongo.repositories.CartRepository;
import hexagonal.modular.domain.ecommerce.web.dtos.carts.CreateCartRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartCreationUseCaseTest {

    @Mock
    private ApplicationEventPublisher eventListener;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private List<ICartRule> rules;

    @InjectMocks
    private CartCreationUseCase useCase;

    @Test
    @DisplayName("Should create cart successfully")
    void shouldCreateCartSuccessfully() {
        var request = new CreateCartRequest("consumer-123", Collections.emptyList());

        when(cartRepository.save(any(CartDocument.class))).thenReturn(new CartDocument());

        useCase.execute(request);
        verify(cartRepository).save(any(CartDocument.class));
    }
}
