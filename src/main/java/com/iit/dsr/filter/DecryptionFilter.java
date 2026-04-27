package com.iit.dsr.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iit.dsr.service.EncryptionService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.IOException;

@Component
@Order(1)
@Log4j2
public class DecryptionFilter implements Filter {

    @Autowired
    private EncryptionService encryptionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI();

        // Skip decryption for the key exchange endpoint
        if (path.contains("/keyExchange")) {
            chain.doFilter(request, response);
            return;
        }

        // Skip if no session or no AES key in session yet
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("aesKey") == null) {
            log.warn("No AES key in session — skipping decryption for: {}", path);
            chain.doFilter(request, response);
            return;
        }

        // Only decrypt requests that have a body (POST, PUT, PATCH)
        String method = httpRequest.getMethod();
        if (!method.equals("POST") && !method.equals("PUT")
                && !method.equals("PATCH") && !method.equals("DELETE")) {
            chain.doFilter(request, response);
            return;
        }

        SecretKey aesKey = (SecretKey) session.getAttribute("aesKey");

        try {
            // Read encrypted body: { iv, ciphertext }
            byte[] bodyBytes = httpRequest.getInputStream().readAllBytes();
            String body = new String(bodyBytes);
            JsonNode node = objectMapper.readTree(body);

            String iv = node.get("iv").asText();
            String ciphertext = node.get("ciphertext").asText();

            // Decrypt to plain JSON
            String decryptedBody = encryptionService.decrypt(iv, ciphertext, aesKey);
            log.info("Request decrypted successfully for: {}", path);

            // Wrap decrypted body back into the request and continue
            CachedBodyHttpServletRequest wrappedRequest =
                    new CachedBodyHttpServletRequest(httpRequest, decryptedBody.getBytes());
            chain.doFilter(wrappedRequest, response);

        } catch (Exception e) {
            log.error("Decryption failed for path: {} — {}", path, e.getMessage());
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Decryption failed");
        }
    }
}