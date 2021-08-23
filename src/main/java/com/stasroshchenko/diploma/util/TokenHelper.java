package com.stasroshchenko.diploma.util;

import com.stasroshchenko.diploma.entity.user.ApplicationUser;
import com.stasroshchenko.diploma.entity.ConfirmationToken;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Util final class for creation of confirmation tokens
 * @author staffsterr2000
 * @version 1.0
 * @see ConfirmationToken
 */
public final class TokenHelper {

    /**
     * Time, in minutes, that the token 'lives'
     */
    public final static Integer TOKEN_EXPIRE_TIMEOUT_MINUTES = 15;

    /**
     * Creates confirmation token and links it to the user, that has to confirm it
     * @param user User, that has to confirm the token to become enabled
     * @return The confirmation token
     * @since 1.0
     * @see ConfirmationToken
     * @see ApplicationUser
     */
    public static ConfirmationToken createConfirmationToken(ApplicationUser user) {
        return new ConfirmationToken(
                createStringToken(),
                user,
                LocalDateTime.now(),
                LocalDateTime.now()
                        .plusMinutes(TOKEN_EXPIRE_TIMEOUT_MINUTES)
        );
    }

    /**
     * Generates random string UUID
     * @return generated UUID
     * @since 1.0
     */
    public static String createStringToken() {
        return UUID.randomUUID().toString();
    }

}
