package com.security.pki.utils;

import com.security.pki.core.AESEncryptionUtil;
import com.security.pki.core.RSAEncryptionUtil;
import com.security.pki.core.RandomGenerator;
import com.security.pki.dto.PKIClientData;
import com.security.pki.dto.PkiData;
import com.security.pki.exception.ClientResponse;
import com.security.pki.exception.CoreClientException;
import com.security.pki.wrapper.RequestWrapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.util.Base64;

@NoArgsConstructor
@Slf4j
public class SecurityUtil {

    protected static PkiData encryptPayloadAndGenerateSignature(String payload, String privateKey, String publicKey) throws CoreClientException {
        try {
            SecretKey secretKey = AESEncryptionUtil.generateSecretKey();
            String encodeSecreteKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            byte[] encryptedSecretKey = RSAEncryptionUtil.encrypt(encodeSecreteKey, publicKey);
            String finalSecretKey = Base64.getEncoder().encodeToString(encryptedSecretKey);
            String encodePayload = AESEncryptionUtil.encryptWithSecreteKey(payload, secretKey);
            String signature = generateSignature(encodePayload, new String(Base64.getDecoder().decode(privateKey)));
            PkiData pkiData = new PkiData();
            pkiData.setData(encodePayload);
            pkiData.setSecretKey(finalSecretKey);
            pkiData.setSignature(signature);
            return pkiData;
        } catch (Exception ex) {
            log.error("Error occured while encryting data error:{} stack:{}", ex.getMessage(), ex);
            throw new CoreClientException(ClientResponse.AUTHORIZATION_FAILED.getCode(), ClientResponse.AUTHORIZATION_FAILED.getValue());
        }
    }

    protected static PKIClientData encryptPayloadAndGenerateSignatureWithClientKey(String payload, String privateKey, String publicKey, String clientKey) throws CoreClientException {
        try {
            String secretKey = RandomGenerator.getAlphaNumericString(32);
            String encodePayload = AESEncryptionUtil.encryptWithPlainSecreteKey(payload, secretKey);
            byte[] encryptedSecretKey = RSAEncryptionUtil.encrypt(secretKey, publicKey);
            String finalSecretKey = Base64.getEncoder().encodeToString(encryptedSecretKey);
            String signature = generateSignature(encodePayload, new String(Base64.getDecoder().decode(privateKey)));
            return PKIClientData.builder().data(encodePayload).signature(signature).secretKey(finalSecretKey).clientKey(clientKey).build();
        } catch (Exception ex) {
            log.error("Error occured while encryting data error:{} stack:{}", ex.getMessage(), ex);
            throw new CoreClientException(ClientResponse.AUTHORIZATION_FAILED.getCode(), ClientResponse.AUTHORIZATION_FAILED.getValue());
        }
    }

    private static String generateSignature(String payload, String privateKey) {
        try {
            return Base64.getEncoder().encodeToString(RSAEncryptionUtil.generateSignature(payload.getBytes(), privateKey));
        } catch (Exception var3) {
            Exception e = var3;
            log.error("Exception : ", e);
            return null;
        }
    }

    protected static String responseValidator(String payload, String publicKey, String privateKey) throws CoreClientException {
        try {
            RequestWrapper requestWrapper = (RequestWrapper) JacksonUtil.get(payload, RequestWrapper.class);
            boolean verified = validateSignature(requestWrapper.getSignature(), requestWrapper.getData(), publicKey);
            byte[] decodedSecretKey = Base64.getDecoder().decode(requestWrapper.getSecretKey());
            String plainSecretKey = RSAEncryptionUtil.decrypt(decodedSecretKey, new String(Base64.getDecoder().decode(privateKey)));
            String data = AESEncryptionUtil.decryptWithSecreteKey(requestWrapper.getData(), AESEncryptionUtil.getSecretKey(plainSecretKey));
            if (verified) {
                return data;
            } else {
                log.error("Error occurred while validating signature.");
                throw new CoreClientException(ClientResponse.INVALID_TOKEN_SIGNATURE.getCode(), ClientResponse.INVALID_TOKEN_SIGNATURE.getValue());
            }
        } catch (Exception var8) {
            Exception e = var8;
            log.error("Error occurred while validating signature. :: {}", e.getMessage());
            throw new CoreClientException(ClientResponse.INVALID_TOKEN_SIGNATURE.getCode(), ClientResponse.INVALID_TOKEN_SIGNATURE.getValue());
        }
    }

    private static boolean validateSignature(String receivedSignature, String payload, String publicKey) throws CoreClientException {
        try {
            return RSAEncryptionUtil.verifySignature(payload, publicKey, Base64.getDecoder().decode(receivedSignature));
        } catch (Exception var4) {
            Exception e = var4;
            log.error("Error occurred while validating signature. :: {}", e.getMessage());
            throw new CoreClientException(ClientResponse.INVALID_TOKEN_SIGNATURE.getCode(), ClientResponse.INVALID_TOKEN_SIGNATURE.getValue());
        }
    }
}
