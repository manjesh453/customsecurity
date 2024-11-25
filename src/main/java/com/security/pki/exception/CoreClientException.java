package com.security.pki.exception;

import lombok.Getter;

@Getter
public class CoreClientException extends Exception{
    private final String code;
    private final String message;

    public CoreClientException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
