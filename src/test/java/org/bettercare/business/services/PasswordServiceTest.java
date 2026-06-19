package org.bettercare.business.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordServiceTest {

    private final PasswordService passwordService = new PasswordService();

    @Test
    void hashesPasswordAndMatchesRawPassword() {
        String hashed = passwordService.hash("password123");

        assertNotEquals("password123", hashed);
        assertTrue(passwordService.matches("password123", hashed));
        assertFalse(passwordService.matches("wrong-password", hashed));
    }
}
