package com.memento.tech.backoffice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BackofficeException extends RuntimeException {

    private final String exceptionLogMessage;

    private final String exceptionMessage;

}