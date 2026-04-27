package com.iit.dsr.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iit.dsr.service.EncryptionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.crypto.SecretKey;
import java.util.Map;

@RestControllerAdvice
@Log4j2
public class EncryptedResponseAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        // Skip encryption for binary/resource responses (PDF, blob)
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        String path = request.getURI().getPath();

        // Skip encryption for key exchange endpoint
        if (path.contains("/keyExchange")) {
            return body;
        }

        // Skip encryption for PDF/report endpoints
        if (path.contains("/generateDsrReport")) {
            return body;
        }

        // Retrieve session and AES key
        HttpServletRequest servletRequest =
                ((ServletServerHttpRequest) request).getServletRequest();
        HttpSession session = servletRequest.getSession(false);

        if (session == null || session.getAttribute("aesKey") == null) {
            log.warn("No AES key in session — skipping encryption for: {}", path);
            return body;
        }

        SecretKey aesKey = (SecretKey) session.getAttribute("aesKey");

        try {
            // Serialize response body to JSON string
            String json = objectMapper.writeValueAsString(body);

            // Encrypt and return { iv, ciphertext }
            Map<String, String> encrypted = encryptionService.encrypt(json, aesKey);
            log.info("Response encrypted successfully for: {}", path);
            return encrypted;

        } catch (Exception e) {
            log.error("Response encryption failed for path: {} — {}", path, e.getMessage());
            return body; // fail safe — return plain body if encryption fails
        }
    }
}