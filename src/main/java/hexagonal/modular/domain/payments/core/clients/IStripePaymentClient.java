package hexagonal.modular.domain.payments.core.clients;

import hexagonal.modular.domain.payments.core.configs.StripeFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "stripePaymentClient",
        url = "https://api.stripe.com/v1",
        configuration = StripeFeignConfig.class)
public interface IStripePaymentClient {

    @PostMapping(value = "/payment_intents",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    String createPaymentIntent(@RequestBody MultiValueMap<String, String> formParams);
}
