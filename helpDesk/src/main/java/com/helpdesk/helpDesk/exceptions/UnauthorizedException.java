package com.helpdesk.helpDesk.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para denegar acceso a recursos por falta de permisos.
 */
@ResponseStatus(HttpStatus.FORBIDDEN) // Esto hace que Spring devuelva automáticamente 403
public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(String mensaje) {
        super(mensaje);
    }
}
