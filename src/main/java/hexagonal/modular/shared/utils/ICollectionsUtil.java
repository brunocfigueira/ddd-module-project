package hexagonal.modular.shared.utils;

import java.util.List;
import java.util.Objects;

/**
 * Utility class for common collection operations.
 * Provides methods to check if a list is empty or null.
 */
public interface ICollectionsUtil {
    /**
     * Checks if the provided list is either null or empty.
     *
     * @param list the list to check
     * @return true if the list is null or empty, false otherwise
     */
    static boolean isEmptyOrNull(List<?> list) {
        return Objects.isNull(list) || list.isEmpty();
    }

    /**
     * Checks if the provided list has elements (i.e., is not null and not empty).
     *
     * @param list the list to check
     * @return true if the list has elements, false if it is null or empty
     */
    static boolean hasElements(List<?> list) {
        return !isEmptyOrNull(list);
    }
}

