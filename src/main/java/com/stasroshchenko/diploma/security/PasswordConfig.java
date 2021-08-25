package com.stasroshchenko.diploma.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Creates and configures password encoder.
 * @author staffsterr2000
 * @version 1.0
 */
@Configuration
public class PasswordConfig {

    /**
     * Creates bean for encoding/decoding users' passwords
     * @return password encoder
     * @since 1.0
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
