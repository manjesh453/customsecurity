package com.security.pki.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CoreClientException.class)
    public ResponseEntity<ClientErrorMessage> restError(CoreClientException ex) {
        if (log.isDebugEnabled()) {
            log.error(ex.getMessage(), ex);
        } else {
            log.error("Error: {}", ex.getMessage());
        }
        return new ResponseEntity<>(new ClientErrorMessage(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
