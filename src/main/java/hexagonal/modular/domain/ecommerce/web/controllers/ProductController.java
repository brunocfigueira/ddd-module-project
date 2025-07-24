package hexagonal.modular.domain.ecommerce.web.controllers;

import hexagonal.modular.domain.ecommerce.core.usecases.products.*;
import hexagonal.modular.domain.ecommerce.web.dtos.products.CreateProductRequest;
import hexagonal.modular.domain.ecommerce.web.dtos.products.ProductResponse;
import hexagonal.modular.domain.ecommerce.web.dtos.products.UpdateProductRequest;
import hexagonal.modular.shared.utils.IFriendlyMessageUtil;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductCreationUseCase creationUseCase;
    private final ProductDetailsUseCase detailsUseCase;
    private final ProductUpdateUseCase updateUseCase;
    private final ProductDeletionUseCase deletionUseCase;
    private final ProductSearchUseCase searchUseCase;

    public ProductController(ProductCreationUseCase creationUseCase, ProductDetailsUseCase detailsUseCase, ProductUpdateUseCase updateUseCase, ProductDeletionUseCase deletionUseCase, ProductSearchUseCase searchUseCase) {
        this.creationUseCase = creationUseCase;
        this.detailsUseCase = detailsUseCase;
        this.updateUseCase = updateUseCase;
        this.deletionUseCase = deletionUseCase;
        this.searchUseCase = searchUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestBody @Valid CreateProductRequest request, UriComponentsBuilder builder) {
        var id = creationUseCase.execute(request);
        var uri = builder.path("products/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).body(IFriendlyMessageUtil.CREATED_RECORD_SUCCESSFULLY);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> read(@PathVariable String id) {
        var response = detailsUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> update(@PathVariable String id, @RequestBody @Valid UpdateProductRequest request) {
        UpdateProductRequest.compareId(id, request);
        updateUseCase.execute(request);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> delete(@PathVariable String id) {
        deletionUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<ProductResponse>> search(@ParameterObject @PageableDefault(size = 10) Pageable request) {
        var response = searchUseCase.execute(request);
        return ResponseEntity.ok(response);
    }
}
