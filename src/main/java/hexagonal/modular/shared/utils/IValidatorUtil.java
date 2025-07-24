package hexagonal.modular.shared.utils;

import hexagonal.modular.shared.exceptions.BusinessRuleException;

import static hexagonal.modular.shared.utils.IStringUtil.isBlank;

public interface IValidatorUtil {
    static void requireNonNull(Object value, String message) {
        if (value == null) {
            throw new BusinessRuleException(message);
        }
    }

    static void requireNotBlank(String value, String message) {
        if (isBlank(value)) {
            throw new BusinessRuleException(message);
        }
    }
}
