package hexagonal.modular.shared.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import hexagonal.modular.shared.exceptions.dtos.FieldErrorDTO;
import hexagonal.modular.shared.exceptions.dtos.InternalResponseDTO;
import hexagonal.modular.shared.exceptions.enums.ProblemTypeEnum;
import hexagonal.modular.shared.utils.ICollectionsUtil;
import hexagonal.modular.shared.utils.IInternalMessageUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalHandlerExceptionAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalHandlerExceptionAdvice.class);

    private InternalResponseDTO buildResponse(HttpStatus status, ProblemTypeEnum type, String message) {
        return new InternalResponseDTO(status, type, message);
    }

    private InternalResponseDTO buildResponse(HttpStatus status, ProblemTypeEnum type, String message, List<FieldErrorDTO> fieldErrors) {
        return new InternalResponseDTO(status, type, message, fieldErrors);
    }

    private String joinPatch(MismatchedInputException ex) {
        return ex.getPath().stream().map(JsonMappingException.Reference::getFieldName).collect(Collectors.joining("."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        LOGGER.error(ex.getMessage(), ex);
        var fieldErrors = ex.getBindingResult().getFieldErrors().stream().map(FieldErrorDTO::new).toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildResponse(HttpStatus.BAD_REQUEST, ProblemTypeEnum.INVALID_FIELD, IInternalMessageUtil.INVALID_FIELD, fieldErrors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        LOGGER.error(ex.getMessage(), ex);
        var fieldErrors = ex.getConstraintViolations().stream().map(FieldErrorDTO::new).toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildResponse(HttpStatus.BAD_REQUEST, ProblemTypeEnum.INVALID_FIELD, IInternalMessageUtil.INVALID_FIELD, fieldErrors));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        var message = String.format(IInternalMessageUtil.RESOURCE_NOT_FOUND, ex.getRequestURL());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildResponse(HttpStatus.NOT_FOUND, ProblemTypeEnum.RESOURCE_NOT_FOUND, message));
    }

    @ExceptionHandler(PropertyBindingException.class)
    public ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex) {
        var message = String.format(IInternalMessageUtil.PROPERTY_NOT_RECOGNIZED, joinPatch(ex), ex.getReferringClass());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildResponse(HttpStatus.BAD_REQUEST, ProblemTypeEnum.PROPERTY_NOT_RECOGNIZED, message));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        var message = String.format(IInternalMessageUtil.METHOD_NOT_ALLOWED, ex.getMethod());
        var problem = buildResponse(HttpStatus.METHOD_NOT_ALLOWED, ProblemTypeEnum.METHOD_NOT_ALLOWED, message);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ProblemTypeEnum.SYSTEM_FAILURE, IInternalMessageUtil.SYSTEM_FAILURE));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ProblemTypeEnum.SYSTEM_FAILURE, IInternalMessageUtil.SYSTEM_FAILURE));
    }

    @ExceptionHandler(BusinessRuleException.class)
    protected ResponseEntity<Object> handleBusinessException(BusinessRuleException ex) {
        if (ICollectionsUtil.isEmptyOrNull(ex.getErrors())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildResponse(HttpStatus.BAD_REQUEST, ProblemTypeEnum.BUSINESS_RULE_VIOLATION, ex.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildResponse(HttpStatus.BAD_REQUEST, ProblemTypeEnum.INVALID_DATA, ex.getMessage(), ex.getErrors()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(buildResponse(HttpStatus.UNAUTHORIZED, ProblemTypeEnum.UNAUTHORIZED, ex.getMessage()));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Object> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(buildResponse(HttpStatus.UNAUTHORIZED, ProblemTypeEnum.UNAUTHORIZED, ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(buildResponse(HttpStatus.FORBIDDEN, ProblemTypeEnum.ACCESS_DENIED, ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildResponse(HttpStatus.NOT_FOUND, ProblemTypeEnum.RESOURCE_NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<Object> handleEntityAlreadyExistsException(EntityAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(buildResponse(HttpStatus.CONFLICT, ProblemTypeEnum.BUSINESS_RULE_VIOLATION, ex.getMessage()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildResponse(HttpStatus.NOT_FOUND, ProblemTypeEnum.RESOURCE_NOT_FOUND, ex.getMessage()));
    }
}
