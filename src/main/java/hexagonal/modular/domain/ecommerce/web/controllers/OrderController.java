package hexagonal.modular.domain.ecommerce.web.controllers;

import hexagonal.modular.domain.ecommerce.core.usecases.orders.OrderCreationUseCase;
import hexagonal.modular.domain.ecommerce.core.usecases.orders.OrderDetailsUseCase;
import hexagonal.modular.domain.ecommerce.web.dtos.orders.CreateOrderRequest;
import hexagonal.modular.shared.utils.IFriendlyMessageUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderCreationUseCase creationUseCase;
    private final OrderDetailsUseCase detailsUseCase;

    public OrderController(OrderCreationUseCase creationUseCase, OrderDetailsUseCase detailsUseCase) {
        this.creationUseCase = creationUseCase;
        this.detailsUseCase = detailsUseCase;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestBody @Valid CreateOrderRequest request, UriComponentsBuilder builder) {
        var id = creationUseCase.execute(request);
        var uri = builder.path("orders/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).body(IFriendlyMessageUtil.CREATED_RECORD_SUCCESSFULLY);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> read(@PathVariable String id) {
        var response = detailsUseCase.execute(id);
        return ResponseEntity.ok(response);
    }
}
