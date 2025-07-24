package hexagonal.modular.domain.ecommerce.web.dtos.carts;

import hexagonal.modular.shared.exceptions.BusinessRuleException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateCartRequest(@NotBlank String id, @Valid @NotNull @NotEmpty List<CartItemsRequest> items) {
    public static void compareId(String id, UpdateCartRequest request) {
        if (!id.equals(request.id)) {
            throw new BusinessRuleException("Request is invalid. The url ID is different from the body parameter");
        }
    }
}
