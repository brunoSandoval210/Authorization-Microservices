package com.common.shared.infrastructure.web;

import com.common.shared.application.dto.ErrorLog;
import com.common.shared.domain.exception.ErrorResponse;
import com.common.shared.domain.exception.PersistenceException;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.common.shared.domain.exception.MappingException;
import com.common.shared.domain.exception.UserNotFoundException;
import com.common.shared.infrastructure.messaging.MessagePublisher;
import com.common.shared.domain.exception.RoleNotFoundException;
import com.common.shared.domain.exception.PermissionNotFoundException;
import com.common.shared.domain.exception.ModuleNotFoundException;
import com.common.shared.domain.exception.UserAlreadyExistsException;
import com.common.shared.domain.exception.RoleAlreadyExistsException;
import com.common.shared.domain.exception.PermissionAlreadyExistsException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final MessagePublisher messagePublisher;

    public GlobalExceptionHandler(MessagePublisher messagePublisher) {
        this.messagePublisher = messagePublisher;
    }

    private void sendErrorLog(String message, String stackTrace, String severity) {
        try {
            var errorLog = new ErrorLog(
                    "common-lib",
                    message,
                    stackTrace,
                    LocalDateTime.now(),
                    severity,
                    null);
            messagePublisher.sendErrorLog(errorLog);
        } catch (Exception ex) {
            log.error("Failed to send error log to audit service", ex);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgNotValid(MethodArgumentNotValidException ex) {
        // ensureLogControl(); removed
        String details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("Validación de parámetros fallida: {}", details);
        ErrorResponse dto = ErrorResponse.of("Validation failed", details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        // ensureLogControl(); removed
        String details = ex.getConstraintViolations()
                .stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .collect(Collectors.joining("; "));
        log.warn("Violaciones de constraints: {}", details);
        ErrorResponse dto = ErrorResponse.of("Validation failed", details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ErrorResponse> handlePersistence(PersistenceException ex) {
        sendErrorLog(ex.getMessage(), java.util.Arrays.toString(ex.getStackTrace()), "ERROR");

        log.error("Excepción de persistencia: {}", ex.getMessage(), ex);
        ErrorResponse dto = ErrorResponse.of(ex.getMessage(), ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
    }

    @ExceptionHandler(MappingException.class)
    public ResponseEntity<ErrorResponse> handleMapping(MappingException ex) {
        sendErrorLog(ex.getMessage(), java.util.Arrays.toString(ex.getStackTrace()), "WARN");

        log.warn("Error de mapeo: {}", ex.getMessage(), ex);
        ErrorResponse dto = ErrorResponse.of(ex.getMessage(), ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    @ExceptionHandler({ UserNotFoundException.class, RoleNotFoundException.class, PermissionNotFoundException.class,
            ModuleNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex) {
        log.info("No encontrado: {}", ex.getMessage());
        ErrorResponse dto = ErrorResponse.of(ex.getMessage(), ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
    }

    @ExceptionHandler({ UserAlreadyExistsException.class, RoleAlreadyExistsException.class,
            PermissionAlreadyExistsException.class })
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex) {
        log.info("Conflicto: {}", ex.getMessage());
        ErrorResponse dto = ErrorResponse.of(ex.getMessage(), ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(dto);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException ex) {
        log.warn("Autenticación fallida: {}", ex.getMessage());
        ErrorResponse dto = ErrorResponse.of(ex.getMessage(), ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(dto);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Acceso denegado: {}", ex.getMessage());
        ErrorResponse dto = ErrorResponse.of(ex.getMessage(), ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(dto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        sendErrorLog(ex.getMessage(), java.util.Arrays.toString(ex.getStackTrace()), "CRITICAL");

        log.error("Excepción no controlada: {}", ex.getMessage(), ex);
        ErrorResponse dto = ErrorResponse.of(ex.getMessage(), ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
    }
}
