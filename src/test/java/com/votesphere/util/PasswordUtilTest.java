package com.votesphere.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordUtilTest {
    @Test
    void hashesPasswordsAndMatchesOnlyTheOriginal() {
        String password = "securePass123";
        String hash = PasswordUtil.hash(password);

        assertNotEquals(password, hash);
        assertTrue(PasswordUtil.matches(password, hash));
        assertFalse(PasswordUtil.matches("not-the-password", hash));
    }
}
