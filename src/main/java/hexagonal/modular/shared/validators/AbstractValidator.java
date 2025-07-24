package hexagonal.modular.shared.validators;

import hexagonal.modular.shared.exceptions.BusinessRuleException;
import hexagonal.modular.shared.exceptions.dtos.FieldErrorDTO;
import hexagonal.modular.shared.utils.ICollectionsUtil;
import hexagonal.modular.shared.utils.IInternalMessageUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractValidator<T> {

    private List<FieldErrorDTO> errors;

    protected abstract void validateObject(T object);

    public final void validate(T object) {
        errors = new ArrayList<>();
        validateObject(object);
        if (hasErrors()) {
            throw new BusinessRuleException(IInternalMessageUtil.INVALID_FIELD, errors);
        }
    }

    protected final boolean hasErrors() {
        return ICollectionsUtil.hasElements(errors);
    }

    protected final void validate(boolean validated, String fieldName, String message) {
        if (!validated) {
            addFieldError(fieldName, message);
        }
    }

    protected final void addFieldError(String fieldName, String message) {
        errors.add(new FieldErrorDTO(fieldName, message));
    }
}