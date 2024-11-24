package com.security.pki.core;

import lombok.NoArgsConstructor;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@NoArgsConstructor
public class AESEncryptionUtil {

    public static String encryptWithSecreteKey(String data, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(1, secretKey);
        return base64Encode(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    public static String encryptWithPlainSecreteKey(String data, String secretKey) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(1, secretKeySpec);
        return base64Encode(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    public static String decryptWithSecreteKey(String strToDecrypt, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(2, secretKey);
        return new String(cipher.doFinal(base64Decode(strToDecrypt)));
    }

    private static byte[] base64Decode(String strToDecrypt) {
        return Base64.getDecoder().decode(strToDecrypt);
    }

    public static String decryptWithPlainSecretKey(String strToDecrypt, String secretKey) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(2, secretKeySpec);
        return new String(cipher.doFinal(base64Decode(strToDecrypt)), StandardCharsets.UTF_8);
    }

    public static SecretKey generateSecretKey() {
        String secretStr = RandomGenerator.getAlphaNumericString(32);
        return getSecretKey(secretStr);
    }

    public static SecretKey getSecretKey(String secretKey) {
        byte[] decodeSecretKey = base64Decode(secretKey);
        return new SecretKeySpec(decodeSecretKey, 0, decodeSecretKey.length, "AES");
    }

    public static String base64Encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
}
