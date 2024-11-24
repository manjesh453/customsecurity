package com.security.pki.wrapper;

import lombok.Data;

@Data
public class RequestWrapper {
    private String clientId;
    private String signature;
    private String data;
    private String secretKey;
}
