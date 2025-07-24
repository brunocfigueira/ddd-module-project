package hexagonal.modular.shared.exceptions.dtos;

import jakarta.validation.ConstraintViolation;
import org.springframework.validation.FieldError;

/**
 * Represents an error related to a specific attribute in a request.
 * This DTO is used to convey validation errors for attributes in a structured format.
 */
public record FieldErrorDTO(String name, String message) {
    public FieldErrorDTO(FieldError fieldError) {
        this(fieldError.getField(), fieldError.getDefaultMessage());
    }

    public FieldErrorDTO(ConstraintViolation<?> constraint) {
        this(String.valueOf(constraint.getPropertyPath()), constraint.getMessage());
    }
}

