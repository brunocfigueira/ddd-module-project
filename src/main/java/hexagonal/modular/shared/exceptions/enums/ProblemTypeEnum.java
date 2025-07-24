package hexagonal.modular.shared.exceptions.enums;

public enum ProblemTypeEnum {
    INVALID_DATA("Invalid Data"),
    INVALID_FIELD("Invalid Field"),
    SYSTEM_FAILURE("System failure"),
    INVALID_PARAMETER("Invalid parameter"),
    PROPERTY_NOT_RECOGNIZED("Property not recognized"),
    INCOMPREHENSIBLE_MESSAGE("Incomprehensible message"),
    RESOURCE_NOT_FOUND("Resource not found"),
    ACCESS_DENIED("Access denied"),
    UNAUTHORIZED("Unauthorized"),
    METHOD_NOT_ALLOWED("Method not allowed"),
    BUSINESS_RULE_VIOLATION("Business rule violation"),
    UNPROCESSABLE_ENTITY("Unprocessable entity");

    private final String title;

    ProblemTypeEnum(String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }
}
