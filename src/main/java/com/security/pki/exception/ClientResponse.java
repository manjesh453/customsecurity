package com.security.pki.exception;

public enum ClientResponse {
    INVALID_TOKEN_SIGNATURE("ES105", "Invalid token signature."),
    AUTHORIZATION_FAILED("ES115", "Failed to authorize data.");

    private String code;
    private String value;

    private ClientResponse(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public String getCode() {
        return this.code;
    }
}

