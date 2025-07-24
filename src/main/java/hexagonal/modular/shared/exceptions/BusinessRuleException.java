package hexagonal.modular.shared.exceptions;

import hexagonal.modular.shared.exceptions.dtos.FieldErrorDTO;

import java.util.ArrayList;
import java.util.List;

public class BusinessRuleException extends RuntimeException {
    private final List<FieldErrorDTO> errors = new ArrayList<>();

    public BusinessRuleException(String message) {
        super(message);
    }

    public BusinessRuleException(String message, List<FieldErrorDTO> errors) {
        super(message);
        this.errors.addAll(errors);
    }

    public List<FieldErrorDTO> getErrors() {
        return errors;
    }
}
