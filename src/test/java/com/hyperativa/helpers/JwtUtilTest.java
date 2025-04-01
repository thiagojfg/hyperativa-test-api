package com.hyperativa.helpers;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private final String secretKey = "ThisIsASecretKeyThatIsAtLeast32CharactersLong!";
    private final JwtUtil jwtUtil = new JwtUtil(secretKey, Duration.ofDays(1));

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken("testuser");
        assertNotNull(token);
    }

    @Test
    void testExtractUsername() {
        String token = jwtUtil.generateToken("testuser");
        assertEquals("testuser", jwtUtil.extractUsername(token));
    }

    @Test
    void testValidateToken() {
        String token = jwtUtil.generateToken("testuser");
        assertTrue(jwtUtil.validateToken(token, "testuser"));
    }

    @Test
    void testTokenExpiration() throws InterruptedException {
        JwtUtil shortLivedJwtUtil = new JwtUtil(secretKey, Duration.ofSeconds(1));
        String token = shortLivedJwtUtil.generateToken("testuser");
        Thread.sleep(2000);
        assertFalse(shortLivedJwtUtil.validateToken(token, "testuser"));
    }
}