package com.security.pki.core;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@NoArgsConstructor
@Slf4j
public class RSAEncryptionUtil {

    public static byte[] generateSignature(byte[] data, String privateKeyPath) throws Exception {
        return generateSignature(data, getPrivateKey(privateKeyPath));
    }

    public static PrivateKey getPrivateKey(String base64PrivateKey) {
        return getPrivateKey(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
    }

    private static PrivateKey getPrivateKey(byte[] keyBytes) {
        PrivateKey privateKey = null;

        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            privateKey = kf.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException var4) {
            log.error("Could not reconstruct the public key, the given algorithm could not be found.");
        } catch (InvalidKeySpecException var5) {
            log.error("Could not reconstruct the private key");
        }

        return privateKey;
    }

    private static byte[] generateSignature(byte[] data, PrivateKey privateKey) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    public static boolean verifySignature(String data, String publicKey, byte[] signature) throws Exception {
        return verifySignature(data, getPublicKey(publicKey), signature);
    }

    public static PublicKey getPublicKey(String base64PublicKey) {
        return getPublicKey(Base64.getDecoder().decode(base64PublicKey.getBytes()));
    }


    private static PublicKey getPublicKey(byte[] keyBytes) {
        PublicKey publicKey = null;

        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            publicKey = kf.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException var4) {
            log.error("Could not reconstruct the public key, the given algorithm could not be found.");
        } catch (InvalidKeySpecException var5) {
            log.error("Could not reconstruct the public key");
        }

        return publicKey;
    }

    private static boolean verifySignature(String data, PublicKey publicKey, byte[] signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(data.getBytes());
        return publicSignature.verify(signature);
    }

    public static byte[] encrypt(String data, String publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(1, getPublicKey(publicKey));
        return cipher.doFinal(data.getBytes());
    }

    public static String decrypt(byte[] data, String privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(2, getPrivateKey(privateKey));
        return new String(cipher.doFinal(data));
    }


}
