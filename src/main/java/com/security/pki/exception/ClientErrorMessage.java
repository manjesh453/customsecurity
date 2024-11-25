package com.security.pki.exception;

import lombok.Getter;

@Getter
public class ClientErrorMessage {

    private final String errorMessage;
    private int code;

    public ClientErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ClientErrorMessage(int code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }
}
