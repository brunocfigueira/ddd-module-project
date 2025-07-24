package hexagonal.modular.domain.ecommerce.web.dtos.products;

import hexagonal.modular.shared.exceptions.BusinessRuleException;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateProductRequest(@NotBlank
                                   String id,
                                   @NotBlank
                                   String name,
                                   @NotNull
                                   @DecimalMin("0.01")
                                   BigDecimal price,
                                   @NotNull boolean active) {
    public static void compareId(String id, UpdateProductRequest request) {
        if (!id.equals(request.id)) {
            throw new BusinessRuleException("Id different from the Body parameter");
        }
    }
}
