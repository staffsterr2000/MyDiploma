package com.stasroshchenko.diploma.util;

import com.stasroshchenko.diploma.dataseed.ApplicationUserDataLoader;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Util final class for generating passport ID.
 * @author staffsterr2000
 * @version 1.0
 * @see ApplicationUserDataLoader
 */
public final class PassportIdGenerator {

    /**
     * Generates and returns accident number from 1_000_000_000 to 10_000_000_000
     * @return random long number
     * @since 1.0
     */
    public static Long generatePassportId() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextLong(1_000_000_000L, 10_000_000_000L);
    }

}
