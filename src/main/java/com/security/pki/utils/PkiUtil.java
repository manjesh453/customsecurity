package com.security.pki.utils;

import com.security.pki.wrapper.CustomHttpRequestWrapper;

public interface PkiUtil {
    String getSignaturePublicKey(CustomHttpRequestWrapper httpRequestWrapper);

    String getEncryptionPublicKey(CustomHttpRequestWrapper httpRequestWrapper);

    String getSignaturePrivateKey(CustomHttpRequestWrapper httpRequestWrapper);

    String getEncryptionPrivateKey(CustomHttpRequestWrapper httpRequestWrapper);
}
