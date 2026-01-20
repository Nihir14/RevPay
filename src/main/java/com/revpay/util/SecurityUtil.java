package com.revpay.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class SecurityUtil {

    // 1. Scramble the password (Hash it)
    public static String hashPassword(String plainPassword) {
        return BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray());
    }

    // 2. Check if a password is correct (Verify it)
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword);
        return result.verified;
    }
}