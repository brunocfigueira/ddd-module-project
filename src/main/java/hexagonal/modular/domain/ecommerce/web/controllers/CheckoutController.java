package hexagonal.modular.domain.ecommerce.web.controllers;

import hexagonal.modular.domain.ecommerce.core.usecases.checkout.ConfirmationOrderUseCase;
import hexagonal.modular.shared.utils.IFriendlyMessageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final ConfirmationOrderUseCase confirmationOrderUseCase;

    public CheckoutController(ConfirmationOrderUseCase confirmationOrderUseCase) {
        this.confirmationOrderUseCase = confirmationOrderUseCase;
    }

    @PutMapping("/confirmOrder/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> confirmOrder(@PathVariable String orderId) {
        confirmationOrderUseCase.execute(orderId);
        return ResponseEntity.ok(IFriendlyMessageUtil.UPDATED_RECORD_SUCCESSFULLY);
    }
}
