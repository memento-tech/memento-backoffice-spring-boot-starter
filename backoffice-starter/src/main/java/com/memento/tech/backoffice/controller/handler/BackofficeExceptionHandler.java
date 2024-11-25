package com.memento.tech.backoffice.controller.handler;

import com.memento.tech.backoffice.dto.BackofficeErrorDTO;
import com.memento.tech.backoffice.exception.BackofficeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class BackofficeExceptionHandler {

    @ExceptionHandler(BackofficeException.class)
    protected ResponseEntity<Object> handleMediaCreationException(BackofficeException exception) {
        log.error(exception.getExceptionLogMessage(), exception);
        return new ResponseEntity<>(new BackofficeErrorDTO(exception.getExceptionMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}