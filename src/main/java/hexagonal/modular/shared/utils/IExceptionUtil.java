package hexagonal.modular.shared.utils;

import java.util.Objects;

/**
 * Utility interface for exception handling.
 * Provides a method to retrieve the root cause of a given Throwable.
 */
public interface IExceptionUtil {
    /**
     * Retrieves the root cause of the provided Throwable.
     *
     * @param throwable the Throwable from which to find the root cause
     * @return the root cause of the Throwable, or null if the input is null
     */
    static Throwable getRootCause(Throwable throwable) {
        if (Objects.isNull(throwable)) {
            return null;
        }

        Throwable cause;
        Throwable rootCause;
        for (cause = throwable; Objects.nonNull(cause.getCause()); cause = rootCause) {
            rootCause = cause.getCause();
        }

        return cause;
    }
}
