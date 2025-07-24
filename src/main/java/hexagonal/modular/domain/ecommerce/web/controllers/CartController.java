package hexagonal.modular.domain.ecommerce.web.controllers;

import hexagonal.modular.domain.ecommerce.core.usecases.carts.CartChangeStatusUseCase;
import hexagonal.modular.domain.ecommerce.core.usecases.carts.CartCreationUseCase;
import hexagonal.modular.domain.ecommerce.core.usecases.carts.CartDetailsUseCase;
import hexagonal.modular.domain.ecommerce.core.usecases.carts.CartUpdateUseCase;
import hexagonal.modular.domain.ecommerce.persistence.mongo.enums.CartStatusEnum;
import hexagonal.modular.domain.ecommerce.web.dtos.carts.CreateCartRequest;
import hexagonal.modular.domain.ecommerce.web.dtos.carts.UpdateCartRequest;
import hexagonal.modular.shared.utils.IFriendlyMessageUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartCreationUseCase creationUseCase;
    private final CartUpdateUseCase updateUseCase;
    private final CartChangeStatusUseCase changeStatusUseCase;
    private final CartDetailsUseCase detailsUseCase;

    public CartController(CartCreationUseCase creationUseCase, CartUpdateUseCase updateUseCase, CartChangeStatusUseCase changeStatusUseCase, CartDetailsUseCase detailsUseCase) {
        this.creationUseCase = creationUseCase;
        this.updateUseCase = updateUseCase;
        this.changeStatusUseCase = changeStatusUseCase;
        this.detailsUseCase = detailsUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestBody @Valid CreateCartRequest request, UriComponentsBuilder builder) {
        var id = creationUseCase.execute(request);
        var uri = builder.path("carts/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).body(IFriendlyMessageUtil.CREATED_RECORD_SUCCESSFULLY);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> update(@PathVariable String id, @RequestBody @Valid UpdateCartRequest request) {
        UpdateCartRequest.compareId(id, request);
        updateUseCase.execute(request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/changeStatusCheckout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> changeStatusToCheckout(@PathVariable String id) {
        changeStatusUseCase.execute(new CartChangeStatusUseCase.Input(id, CartStatusEnum.CHECKOUT));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/changeStatusOpen")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> changeStatusToOpen(@PathVariable String id) {
        changeStatusUseCase.execute(new CartChangeStatusUseCase.Input(id, CartStatusEnum.OPENED));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> read(@PathVariable String id) {
        var response = detailsUseCase.execute(id);
        return ResponseEntity.ok(response);
    }
}
