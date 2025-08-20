package com.employee.payroll.security;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility to generate a secure Base64-encoded secret key for JWT signing.
 * Run this class to generate a key and add it to application.properties as jwt.secret.
 */
public class SecretGenerator {
    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[64]; // Use 64 bytes for HS512 (512 bits)
        random.nextBytes(bytes);
        String secret = Base64.getEncoder().encodeToString(bytes);
        System.out.println("JWT Secret: " + secret);
    }
}