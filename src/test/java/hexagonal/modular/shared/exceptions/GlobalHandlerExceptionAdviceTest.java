package hexagonal.modular.shared.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexagonal.modular.shared.exceptions.dtos.InternalResponseDTO;
import hexagonal.modular.shared.exceptions.enums.ProblemTypeEnum;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalHandlerExceptionAdviceTest {

    @InjectMocks
    private GlobalHandlerExceptionAdvice exceptionHandler;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    private ConstraintViolation<?> mockConstraintViolation(String propertyPath, String message) {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        jakarta.validation.Path path = mock(jakarta.validation.Path.class);
        when(violation.getPropertyPath()).thenReturn(path);
        when(path.toString()).thenReturn(propertyPath);
        when(violation.getMessage()).thenReturn(message);
        return violation;
    }

    private JsonMappingException.Reference createReference(String fieldName) {
        return new JsonMappingException.Reference(null, fieldName);
    }

    @Test
    void handleMethodArgumentNotValidException() throws Exception {
        // Arrange
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("object", "field1", "erro no campo 1"));
        fieldErrors.add(new FieldError("object", "field2", "erro no campo 2"));

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        // Act
        ResponseEntity<Object> responseEntity = exceptionHandler.handleMethodArgumentNotValidException(exception);
        String json = objectMapper.writeValueAsString(responseEntity.getBody());

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        System.out.println("MethodArgumentNotValidException Response:");
        System.out.println(json);

        InternalResponseDTO problem = (InternalResponseDTO) responseEntity.getBody();
        Assertions.assertNotNull(problem);
        assertEquals(ProblemTypeEnum.INVALID_FIELD.title(), problem.title());
        assertEquals(2, problem.fieldErrors().size());
    }

    @Test
    void handleConstraintViolationException() throws Exception {
        // Arrange
        ConstraintViolationException exception = mock(ConstraintViolationException.class);
        Set<ConstraintViolation<?>> violations = Set.of(mockConstraintViolation("field1", "erro no campo 1"), mockConstraintViolation("field2", "erro no campo 2"));

        when(exception.getConstraintViolations()).thenReturn(violations);

        // Act
        ResponseEntity<Object> responseEntity = exceptionHandler.handleConstraintViolationException(exception);
        String json = objectMapper.writeValueAsString(responseEntity.getBody());

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        System.out.println("ConstraintViolationException Response:");
        System.out.println(json);
        InternalResponseDTO problem = (InternalResponseDTO) responseEntity.getBody();
        Assertions.assertNotNull(problem);
        assertEquals(2, problem.fieldErrors().size());
    }

    @Test
    void handleNoHandlerFoundException() throws Exception {
        // Arrange
        var exception = new org.springframework.web.servlet.NoHandlerFoundException("GET", "/notfound", null);
        // Act
        ResponseEntity<Object> responseEntity = exceptionHandler.handleNoHandlerFoundException(exception);
        String json = objectMapper.writeValueAsString(responseEntity.getBody());
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        System.out.println("NoHandlerFoundException Response:");
        System.out.println(json);
        InternalResponseDTO problem = (InternalResponseDTO) responseEntity.getBody();
        Assertions.assertNotNull(problem);
        assertEquals(HttpStatus.NOT_FOUND.value(), problem.statusCode());
    }

    @Test
    void handlePropertyBindingException() throws Exception {
        // Arrange
        var ex = mock(com.fasterxml.jackson.databind.exc.PropertyBindingException.class);
        var ref = createReference("field");
        when(ex.getPath()).thenReturn(List.of(ref));

        // Act
        ResponseEntity<Object> responseEntity = exceptionHandler.handlePropertyBindingException(ex);
        String json = objectMapper.writeValueAsString(responseEntity.getBody());
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        System.out.println("PropertyBindingException Response:");
        System.out.println(json);
        InternalResponseDTO problem = (InternalResponseDTO) responseEntity.getBody();
        Assertions.assertNotNull(problem);
        assertEquals(HttpStatus.BAD_REQUEST.value(), problem.statusCode());
    }

    @Test
    void handleHttpRequestMethodNotSupported() throws Exception {
        // Arrange
        var ex = new org.springframework.web.HttpRequestMethodNotSupportedException("POST");
        // Act
        ResponseEntity<Object> responseEntity = exceptionHandler.handleHttpRequestMethodNotSupported(ex);
        String json = objectMapper.writeValueAsString(responseEntity.getBody());
        // Assert
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, responseEntity.getStatusCode());
        System.out.println("HttpRequestMethodNotSupportedException Response:");
        System.out.println(json);
        InternalResponseDTO problem = (InternalResponseDTO) responseEntity.getBody();
        Assertions.assertNotNull(problem);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), problem.statusCode());
    }

    @Test
    void handleException() throws Exception {
        // Arrange
        Exception ex = new Exception("erro generico");
        // Act
        ResponseEntity<Object> responseEntity = exceptionHandler.handleException(ex);
        String json = objectMapper.writeValueAsString(responseEntity.getBody());
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        System.out.println("Exception Response:");
        System.out.println(json);
        InternalResponseDTO problem = (InternalResponseDTO) responseEntity.getBody();
        Assertions.assertNotNull(problem);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), problem.statusCode());
    }

    @Test
    void handleRuntimeException() throws Exception {
        // Arrange
        RuntimeException ex = new RuntimeException("erro runtime");
        // Act
        ResponseEntity<Object> responseEntity = exceptionHandler.handleRuntimeException(ex);
        String json = objectMapper.writeValueAsString(responseEntity.getBody());
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        System.out.println("RuntimeException Response:");
        System.out.println(json);
        InternalResponseDTO problem = (InternalResponseDTO) responseEntity.getBody();
        Assertions.assertNotNull(problem);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), problem.statusCode());
    }

    @Test
    void handleBusinessExceptionWithoutErrors() throws Exception {
        // Arrange
        var ex = new BusinessRuleException("Erro de negócio");
        // Act
        ResponseEntity<Object> responseEntity = exceptionHandler.handleBusinessException(ex);
        String json = objectMapper.writeValueAsString(responseEntity.getBody());
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        System.out.println("BusinessException sem erros Response:");
        System.out.println(json);
        InternalResponseDTO problem = (InternalResponseDTO) responseEntity.getBody();
        Assertions.assertNotNull(problem);
        assertEquals(HttpStatus.BAD_REQUEST.value(), problem.statusCode());
    }

    @Test
    void handleBusinessException_comErros() throws Exception {
        // Arrange
        var errors = List.of(new hexagonal.modular.shared.exceptions.dtos.FieldErrorDTO("field", "msg"));
        var ex = new BusinessRuleException("msg de negócio", errors);
        // Act
        ResponseEntity<Object> responseEntity = exceptionHandler.handleBusinessException(ex);
        String json = objectMapper.writeValueAsString(responseEntity.getBody());
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        System.out.println("BusinessException com erros Response:");
        System.out.println(json);
        InternalResponseDTO problem = (InternalResponseDTO) responseEntity.getBody();
        Assertions.assertNotNull(problem);
        assertEquals(HttpStatus.BAD_REQUEST.value(), problem.statusCode());
        assertEquals(1, problem.fieldErrors().size());
    }

    @Test
    void handleBadCredentialsException() throws Exception {
        // Arrange
        var ex = new org.springframework.security.authentication.BadCredentialsException("credenciais ruins");
        // Act
        ResponseEntity<Object> responseEntity = exceptionHandler.handleBadCredentialsException(ex);
        String json = objectMapper.writeValueAsString(responseEntity.getBody());
        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        System.out.println("BadCredentialsException Response:");
        System.out.println(json);
        InternalResponseDTO problem = (InternalResponseDTO) responseEntity.getBody();
        Assertions.assertNotNull(problem);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), problem.statusCode());
    }

    @Test
    void handleAuthorizationDeniedException() throws Exception {
        // Arrange
        var ex = new org.springframework.security.authorization.AuthorizationDeniedException("autorização negada");
        // Act
        ResponseEntity<Object> responseEntity = exceptionHandler.handleAuthorizationDeniedException(ex);
        String json = objectMapper.writeValueAsString(responseEntity.getBody());
        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        System.out.println("AuthorizationDeniedException Response:");
        System.out.println(json);
        InternalResponseDTO problem = (InternalResponseDTO) responseEntity.getBody();
        Assertions.assertNotNull(problem);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), problem.statusCode());
    }

    @Test
    void handleAccessDeniedException() throws Exception {
        // Arrange
        var ex = new org.springframework.security.access.AccessDeniedException("acesso negado");
        // Act
        ResponseEntity<Object> responseEntity = exceptionHandler.handleAccessDeniedException(ex);
        String json = objectMapper.writeValueAsString(responseEntity.getBody());
        // Assert
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        System.out.println("AccessDeniedException Response:");
        System.out.println(json);
        InternalResponseDTO problem = (InternalResponseDTO) responseEntity.getBody();
        Assertions.assertNotNull(problem);
        assertEquals(HttpStatus.FORBIDDEN.value(), problem.statusCode());
    }

    @Test
    void handleEntityNotFoundException() throws Exception {
        // Arrange
        var ex = new jakarta.persistence.EntityNotFoundException("não encontrado");
        // Act
        ResponseEntity<Object> responseEntity = exceptionHandler.handleEntityNotFoundException(ex);
        String json = objectMapper.writeValueAsString(responseEntity.getBody());
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        System.out.println("EntityNotFoundException Response:");
        System.out.println(json);
        InternalResponseDTO problem = (InternalResponseDTO) responseEntity.getBody();
        Assertions.assertNotNull(problem);
        assertEquals(HttpStatus.NOT_FOUND.value(), problem.statusCode());
    }

    @Test
    void handleEntityAlreadyExistsException() throws Exception {
        // Arrange
        var ex = new EntityAlreadyExistsException("já existe");
        // Act
        ResponseEntity<Object> responseEntity = exceptionHandler.handleEntityAlreadyExistsException(ex);
        String json = objectMapper.writeValueAsString(responseEntity.getBody());
        // Assert
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        System.out.println("EntityAlreadyExistsException Response:");
        System.out.println(json);
        InternalResponseDTO problem = (InternalResponseDTO) responseEntity.getBody();
        Assertions.assertNotNull(problem);
        assertEquals(HttpStatus.CONFLICT.value(), problem.statusCode());
    }

    @Test
    void handleNoResourceFoundException() throws Exception {
        // Arrange
        var ex = new org.springframework.web.servlet.resource.NoResourceFoundException(HttpMethod.GET, "/notfound");
        // Act
        ResponseEntity<Object> responseEntity = exceptionHandler.handleNoResourceFoundException(ex);
        String json = objectMapper.writeValueAsString(responseEntity.getBody());
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        System.out.println("NoResourceFoundException Response:");
        System.out.println(json);
        InternalResponseDTO problem = (InternalResponseDTO) responseEntity.getBody();
        Assertions.assertNotNull(problem);
        assertEquals(HttpStatus.NOT_FOUND.value(), problem.statusCode());
    }
}

