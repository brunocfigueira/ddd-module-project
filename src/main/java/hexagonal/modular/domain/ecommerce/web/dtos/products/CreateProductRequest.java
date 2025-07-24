package hexagonal.modular.domain.ecommerce.web.dtos.products;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductRequest(@NotBlank
                                   String name,
                                   @NotNull
                                   @DecimalMin("0.01")
                                   BigDecimal price) {
}
