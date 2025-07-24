package hexagonal.modular.domain.payments.core.configs;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackages = "hexagonal.modular.domain.payments.core.clients")
@Configuration
public class StripeFeignConfig {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer " + stripeSecretKey);
            requestTemplate.header("Content-Type", "application/x-www-form-urlencoded");
        };
    }
}
