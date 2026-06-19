package org.bettercare.business.service;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Service
public class PasswordService {

    public String hash(String rawPassword) {
        // Passwords are changed into a hash before they are saved
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }

    public boolean matches(String rawPassword, String storedPassword) {
        // We hash the login password too so we can compare two safe values
        if (rawPassword == null || storedPassword == null) {
            return false;
        }

        return hash(rawPassword).equals(storedPassword);
    }
}