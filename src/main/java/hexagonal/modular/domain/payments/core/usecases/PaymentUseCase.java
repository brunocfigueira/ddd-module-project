package hexagonal.modular.domain.payments.core.usecases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.StripeException;
import hexagonal.modular.domain.payments.core.clients.IStripePaymentClient;
import hexagonal.modular.domain.payments.dtos.PaymentRequest;
import hexagonal.modular.domain.payments.dtos.PaymentResponse;
import hexagonal.modular.domain.payments.dtos.StripePaymentIntentResponse;
import hexagonal.modular.domain.payments.enums.PaymentStatus;
import hexagonal.modular.shared.usecases.IUseCase;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PaymentUseCase implements IUseCase<PaymentRequest, PaymentResponse> {
    private final IStripePaymentClient stripePaymentClient;
    private final ObjectMapper objectMapper;

    public PaymentUseCase(IStripePaymentClient stripePaymentClient, ObjectMapper objectMapper) {
        this.stripePaymentClient = stripePaymentClient;
        this.objectMapper = objectMapper;
    }

    /**
     * @param request o parâmetro de entrada para a execução, do tipo {@code I}
     * @return
     */
    @Override
    public PaymentResponse execute(PaymentRequest request) throws StripeException, JsonProcessingException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("description", "Payment of the order id - " + request.order().getId());
        // params.add("customer", request.order().getCustomerId());
        params.add("amount", convertAmountToCents(request.order().getTotalAmount())); // valor em centavos (5000 = R$50,00)
        params.add("currency", "brl");
        // params.add("confirm", "true");
        // params.add("payment_method_types[0]", "pix");
        params.add("automatic_payment_methods[enabled]", "true");
        params.add("automatic_payment_methods[allow_redirects]", "always");
        // params.add("return_url", "http://localhost:8080/payment/confirm");
        var response = stripePaymentClient.createPaymentIntent(params);
        var stripeResponse = objectMapper.readValue(response, StripePaymentIntentResponse.class);
        return new PaymentResponse(PaymentStatus.APPROVED, stripeResponse.id(), stripeResponse.client_secret());
    }

    private String convertAmountToCents(BigDecimal amount) {
        if (amount == null) {
            return "0";
        }
        return amount.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).toPlainString();
    }
}
