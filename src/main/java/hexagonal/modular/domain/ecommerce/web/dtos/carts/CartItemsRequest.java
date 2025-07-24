package hexagonal.modular.domain.ecommerce.web.dtos.carts;

import com.mongodb.lang.NonNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CartItemsRequest(
        @NotBlank
        String productId,
        @NonNull
        @Min(value = 1)
        Integer quantity) {
}
