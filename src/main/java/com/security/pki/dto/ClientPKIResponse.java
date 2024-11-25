package com.security.pki.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientPKIResponse {
    private String code;
    private String message;
}
