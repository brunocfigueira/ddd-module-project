package hexagonal.modular.shared.exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Component
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAsyncExceptionHandler.class);

    private final GlobalHandlerExceptionAdvice globalHandler;

    public CustomAsyncExceptionHandler(GlobalHandlerExceptionAdvice globalHandler) {
        this.globalHandler = globalHandler;
    }

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... params) {
        LOGGER.error("Async method {} threw uncaught exception with params: {}",
                method.getName(), Arrays.toString(params), throwable);

        // Reutiliza a lógica do GlobalHandlerExceptionAdvice
        try {
            ResponseEntity<Object> response = handleExceptionByType(throwable);
            LOGGER.error("Async exception would return: {}", response.getBody());

            // Aqui você pode implementar ações específicas para eventos assíncronos
            // Como notificar administradores, enviar para dead letter queue, etc.
            handleAsyncSpecificActions(throwable, method, response);

        } catch (Exception e) {
            LOGGER.error("Failed to handle async exception", e);
        }
    }

    private ResponseEntity<Object> handleExceptionByType(Throwable throwable) {
        return switch (throwable) {
            case BusinessRuleException ex -> globalHandler.handleBusinessException(ex);
            case EntityNotFoundException ex -> globalHandler.handleEntityNotFoundException(ex);
            case EntityAlreadyExistsException ex -> globalHandler.handleEntityAlreadyExistsException(ex);
            case ConstraintViolationException ex -> globalHandler.handleConstraintViolationException(ex);
            case RuntimeException ex -> globalHandler.handleRuntimeException(ex);
            case Exception ex -> globalHandler.handleException(ex);
            default -> globalHandler.handleException(new Exception(throwable));
        };
    }

    private void handleAsyncSpecificActions(Throwable throwable, Method method, ResponseEntity<Object> response) {
        // Implementar ações específicas para eventos assíncronos
        // Por exemplo:
        // - Notificar administradores
        // - Enviar para dead letter queue
        // - Implementar retry logic
        // - Registrar métricas de falhas

        if (throwable instanceof BusinessRuleException) {
            // Lógica específica para regras de negócio
            LOGGER.warn("Business rule violation in async method {}: {}", method.getName(), throwable.getMessage());
        }
    }
}