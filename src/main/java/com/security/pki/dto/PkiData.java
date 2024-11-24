package com.security.pki.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PkiData {
    private String signature;
    private String secretKey;
    private String data;
}
