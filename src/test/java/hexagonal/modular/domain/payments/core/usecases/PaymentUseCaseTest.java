package hexagonal.modular.domain.payments.core.usecases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.StripeException;
import hexagonal.modular.domain.ecommerce.persistence.mongo.documents.OrderDocument;
import hexagonal.modular.domain.payments.core.clients.IStripePaymentClient;
import hexagonal.modular.domain.payments.dtos.PaymentRequest;
import hexagonal.modular.domain.payments.dtos.StripePaymentIntentResponse;
import hexagonal.modular.domain.payments.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentUseCaseTest {
    @Mock
    private IStripePaymentClient stripePaymentClient;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private PaymentUseCase paymentUseCase;
    private PaymentRequest paymentRequest;
    private OrderDocument orderDocument;

    @BeforeEach
    void setUp() {
        orderDocument = new OrderDocument();
        orderDocument.setId("order-123");
        orderDocument.setTotalAmount(new BigDecimal("100.00"));
        paymentRequest = new PaymentRequest(orderDocument);
    }

    @Test
    @DisplayName("Should create payment intent successfully")
    void shouldCreatePaymentIntentSuccessfully() throws StripeException, JsonProcessingException {
        var stripeResponse = new StripePaymentIntentResponse("pi_123", "cs_123");
        var stripeResponseJson = "{\"id\":\"pi_123\",\"client_secret\":\"cs_123\"}";
        when(stripePaymentClient.createPaymentIntent(any(MultiValueMap.class))).thenReturn(stripeResponseJson);
        when(objectMapper.readValue(eq(stripeResponseJson), eq(StripePaymentIntentResponse.class))).thenReturn(stripeResponse);
        var paymentResponse = paymentUseCase.execute(paymentRequest);
        assertNotNull(paymentResponse);
        assertEquals(PaymentStatus.APPROVED, paymentResponse.status());
        assertEquals("pi_123", paymentResponse.paymentReferenceKey());
        assertEquals("cs_123", paymentResponse.clientSecret());
    }

    @Test
    @DisplayName("Should handle null amount when converting to cents")
    void shouldHandleNullAmountWhenConvertingToCents() throws StripeException, JsonProcessingException {
        orderDocument.setTotalAmount(null);
        paymentRequest = new PaymentRequest(orderDocument);
        var stripeResponse = new StripePaymentIntentResponse("pi_123", "cs_123");
        var stripeResponseJson = "{\"id\":\"pi_123\",\"client_secret\":\"cs_123\"}";
        when(stripePaymentClient.createPaymentIntent(any(MultiValueMap.class))).thenReturn(stripeResponseJson);
        when(objectMapper.readValue(eq(stripeResponseJson), eq(StripePaymentIntentResponse.class))).thenReturn(stripeResponse);
        var paymentResponse = paymentUseCase.execute(paymentRequest);
        assertNotNull(paymentResponse);
        assertEquals(PaymentStatus.APPROVED, paymentResponse.status());
    }
}