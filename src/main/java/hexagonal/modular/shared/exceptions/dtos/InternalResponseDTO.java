package hexagonal.modular.shared.exceptions.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import hexagonal.modular.shared.exceptions.enums.ProblemTypeEnum;
import org.springframework.http.HttpStatus;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record InternalResponseDTO(int statusCode, String title, String message, List<FieldErrorDTO> fieldErrors) {

    public InternalResponseDTO(HttpStatus status, ProblemTypeEnum type, String message) {
        this(status.value(), type.title(), message, null);
    }

    public InternalResponseDTO(HttpStatus status, ProblemTypeEnum type, String message, List<FieldErrorDTO> fieldErrors) {
        this(status.value(), type.title(), message, fieldErrors);
    }
}
