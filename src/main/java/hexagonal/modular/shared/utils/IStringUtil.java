package hexagonal.modular.shared.utils;

import org.apache.commons.text.StringEscapeUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

/**
 * This interface provides utility methods for string manipulation, including checks for blank strings,
 * conversion of strings to maps, and sanitization of strings.
 */
public interface IStringUtil {
    /**
     * Checks if a string is blank (null or empty after trimming).
     *
     * @param value the string to check
     * @return true if the string is blank, false otherwise
     */
    static boolean isBlank(String value) {
        return Objects.isNull(value) || value.trim().isEmpty();
    }

    /**
     * Checks if a string is not blank (not null and not empty after trimming).
     *
     * @param value the string to check
     * @return true if the string is not blank, false otherwise
     */
    static boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    /**
     * Checks if a string is empty (null or empty).
     *
     * @param value the string to check
     * @return Map<String, String>
     */
    static Map<String, String> convertStringToMap(String value) {
        Map<String, String> map = new HashMap<>();
        StringTokenizer tokenizer = new StringTokenizer(value, " ");

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            String[] keyValue = token.split("=");
            map.put(keyValue[0], keyValue[1]);
        }

        return map;
    }

    /**
     * Sanitizes a string by escaping Java characters and removing line breaks.
     *
     * @param value the string to sanitize
     * @return the sanitized string, or null if the input is blank
     */
    static String sanitize(String value) {
        if (isBlank(value)) {
            return null;
        }
        return StringEscapeUtils.escapeJava(value.replaceAll("[\\r\\n]", "")).trim();
    }

    /**
     * Sanitizes a string by removing line breaks and non-printable characters.
     *
     * @param value the string to sanitize
     * @return the sanitized string, or null if the input is blank
     */
    static String sanitizeLogInjection(String value) {
        if (isBlank(value)) {
            return null;
        }
        return value.replaceAll("[\\r\\n]", "").replaceAll("[^\\x20-\\x7E]", "");
    }
}
