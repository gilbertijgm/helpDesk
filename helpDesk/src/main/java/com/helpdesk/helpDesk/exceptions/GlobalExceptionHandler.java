package com.helpdesk.helpDesk.exceptions;

import com.helpdesk.helpDesk.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler  {

    /**
     * Manejador para errores de validación de campos (anotaciones como @NotNull, @Size, etc.).
     * Devuelve una lista de errores en el campo "data".
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        // Extraemos y formateamos los errores campo por campo
        List<String> errores = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());

        ApiResponse<List<String>> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "Errores de validación",
                errores
        );

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Manejador para errores de JSON mal formateado o valores inválidos (como enums no reconocidos).
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<String>> handleJsonParseError(HttpMessageNotReadableException ex) {
        String mensaje;

        if (ex.getMessage() != null && ex.getMessage().contains("Task$Status")) {
            mensaje = "El estado enviado es inválido o está vacío. Valores permitidos: Pendiente, Desarrollo, Completada, Cancelada.";
        } else {
            mensaje = "Error en el formato del JSON. Verificá los datos enviados.";
        }

        ApiResponse<String> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                mensaje,
                null
        );

        return ResponseEntity.badRequest().body(response);
    }

    /*
    * Manejador para los casos de acceso no autorizados
    * */
    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<ApiResponse<Object>> handleForbiddenAccessException(ForbiddenAccessException ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }


    /**
     * Manejador para recursos no encontrados. Lanza tu excepción personalizada.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    /**
     * Manejador para recursos no registrados. Lanza tu excepción personalizada.
     */
    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<ApiResponse<?>> handleNoContent(NoContentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(404, ex.getMessage(), Collections.emptyList()));
    }

    /**
     * Manejador para argumentos inválidos que no pasaron por validaciones con anotaciones.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Manejador para excepciones que lanzás con ResponseStatusException manualmente.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<Object>> handleResponseStatusException(ResponseStatusException ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                ex.getStatusCode().value(),
                ex.getReason(),
                null
        );

        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }

    /**
     * Manejador cuando un @PathVariable esperado no fue enviado en la URL.
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingPathVariableException(MissingPathVariableException ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "El parámetro de la URL está ausente.",
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Manejador global para errores no controlados. Siempre útil tener uno general por seguridad.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex) {
        // Podés loguearlo aquí si querés
        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocurrió un error inesperado.",
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


    /**
     * Excepción personalizada para denegar acceso a recursos por falta de permisos.
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnauthorized(UnauthorizedException ex) {
        ApiResponse<Object> response = new ApiResponse<>(403, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}
