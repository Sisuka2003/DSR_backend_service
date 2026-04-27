package com.iit.dsr.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

@Service
@Log4j2
public class EncryptionService {

    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;
    public Map<String, String> performKeyExchange(String clientPublicKeyB64,
                                                  HttpSession session) throws Exception {
        // 1. Generate server ECDH keypair
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
        kpg.initialize(new ECGenParameterSpec("secp256r1"));
        KeyPair serverKeyPair = kpg.generateKeyPair();

        // 2. Import client public key
        byte[] clientPubBytes = Base64.getDecoder().decode(clientPublicKeyB64);
        KeyFactory kf = KeyFactory.getInstance("EC");
        PublicKey clientPublicKey = kf.generatePublic(new X509EncodedKeySpec(clientPubBytes));

        // 3. ECDH — derive shared secret
        KeyAgreement ka = KeyAgreement.getInstance("ECDH");
        ka.init(serverKeyPair.getPrivate());
        ka.doPhase(clientPublicKey, true);
        byte[] sharedSecret = ka.generateSecret();


        // 4. Use first 32 bytes of shared secret directly as AES-256 key
        //    This matches WebCrypto's ECDH deriveKey behavior
        byte[] aesKeyBytes = Arrays.copyOf(sharedSecret, 32);
        SecretKey aesKey = new SecretKeySpec(aesKeyBytes, "AES");

        // 5. Store in session
        session.setAttribute("aesKey", aesKey);

        // 6. Return server public key to frontend
        String serverPublicKeyB64 = Base64.getEncoder()
                .encodeToString(serverKeyPair.getPublic().getEncoded());

        return Map.of("publicKey", serverPublicKeyB64);
    }

    public Map<String, String> encrypt(String plaintext, SecretKey key) throws Exception {
        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);

        byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        String ivB64 = Base64.getEncoder().encodeToString(iv);
        String ciphertextB64 = Base64.getEncoder().encodeToString(ciphertext);

        return Map.of(
                "iv", ivB64,
                "ciphertext", ciphertextB64
        );
    }

    public String decrypt(String ivB64, String ciphertextB64, SecretKey key) throws Exception {
        // Decode IV and ciphertext from Base64
        byte[] iv = Base64.getDecoder().decode(ivB64);
        byte[] ciphertext = Base64.getDecoder().decode(ciphertextB64);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);

        byte[] plaintext = cipher.doFinal(ciphertext);

        return new String(plaintext, StandardCharsets.UTF_8);
    }
}