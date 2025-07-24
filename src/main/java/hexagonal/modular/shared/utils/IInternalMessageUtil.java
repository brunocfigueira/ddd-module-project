package hexagonal.modular.shared.utils;

/**
 * This interface defines a constant for internal messages used across the application.
 * It is intended to be used for validation messages or other internal communication.
 */
public interface IInternalMessageUtil {
    static final String INVALID_FIELD = "One or more fields are invalid. Please fill them correctly and try again.";
    static final String RESOURCE_NOT_FOUND = "The resource '%s' you tried to access does not exist.";
    static final String PROPERTY_NOT_RECOGNIZED = "The property '%s' is not recognized for the model '%s'";
    static final String METHOD_NOT_ALLOWED = "The method '%s' is not allowed for request.";
    static final String SYSTEM_FAILURE = "An unexpected internal system error has occurred. Please try again, and if the problem persists, contact the system administrator.";
}
