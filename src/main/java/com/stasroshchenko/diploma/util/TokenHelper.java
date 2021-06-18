package com.stasroshchenko.diploma.util;

import com.stasroshchenko.diploma.entity.user.ApplicationUser;
import com.stasroshchenko.diploma.entity.ConfirmationToken;

import java.time.LocalDateTime;
import java.util.UUID;

public final class TokenHelper {
    public final static Integer TOKEN_EXPIRE_TIMEOUT_MINUTES = 15;

    public static ConfirmationToken createConfirmationToken(ApplicationUser user) {
        return new ConfirmationToken(
                createStringToken(),
                user,
                LocalDateTime.now(),
                LocalDateTime.now()
                        .plusMinutes(TOKEN_EXPIRE_TIMEOUT_MINUTES)
        );
    }

    public static String createStringToken() {
        return UUID.randomUUID().toString();
    }

}
