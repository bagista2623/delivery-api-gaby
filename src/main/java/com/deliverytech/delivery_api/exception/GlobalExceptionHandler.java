package com.deliverytech.delivery_api.exception;

import com.deliverytech.delivery_api.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 400 - Bean Validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest req
    ) {

        Map<String, String> detalhes = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        f -> f.getField(),
                        f -> f.getDefaultMessage(),
                        (a,b) -> a
                ));

        ErrorResponse err = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(400)
                .error("Dados inválidos")
                .message("Erro de validação nos dados enviados")
                .path(req.getRequestURI())
                .details(detalhes)
                .build();

        return ResponseEntity.badRequest().body(err);
    }

    // 400 - @Valid em URL params
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest req
    ) {
        ErrorResponse err = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(400)
                .error("Parâmetros inválidos")
                .message(ex.getMessage())
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.badRequest().body(err);
    }

    // 404 - Entity Not Found
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            EntityNotFoundException ex,
            HttpServletRequest req
    ) {

        ErrorResponse err = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(404)
                .error("Não encontrado")
                .message(ex.getMessage())
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.status(404).body(err);
    }

    // 409 - Conflitos
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(
            ConflictException ex, HttpServletRequest req
    ) {
        ErrorResponse err = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(409)
                .error("Conflito de dados")
                .message(ex.getMessage())
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.status(409).body(err);
    }

    // 422 - Regra de negócio
    @ExceptionHandler({ BusinessException.class, ValidationException.class })
    public ResponseEntity<ErrorResponse> handleBusiness(
            RuntimeException ex, HttpServletRequest req
    ) {
        ErrorResponse err = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(422)
                .error("Regra de negócio violada")
                .message(ex.getMessage())
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.status(422).body(err);
    }

    // 500 - Erro desconhecido
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(
            Exception ex, HttpServletRequest req
    ) {

        ErrorResponse err = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(500)
                .error("Erro interno do servidor")
                .message(ex.getMessage())
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.status(500).body(err);
    }
}
