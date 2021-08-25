package com.stasroshchenko.diploma.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configures web security of the application.
 * @author staffsterr2000
 * @version 1.0
 */
@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Password encoder for {@link DaoAuthenticationProvider}
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Service for working with users for {@link DaoAuthenticationProvider}
     */
    private final UserDetailsService userDetailsService;



    /**
     * Configures http security
     * @param http builder for http security configuration
     * @throws Exception exceptions that configuration may cause
     * @since 1.0
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // disable csrf
                .csrf().disable()
                .authorizeRequests()

                // allow access to all following resources without authentication
                .antMatchers("/", "/registration/**", "/verification/**",
                        "/id/**", "/index", "/css/**", "/img/**", "/js/**").permitAll()

                // other requests must be authenticated
                .anyRequest().authenticated()
                .and()

                // login setting
                .formLogin()
                // link for login
                    .loginPage("/login").permitAll()
                    // redirect after successful login
                    .defaultSuccessUrl("/", true)
                    // redirect after failure
                    .failureForwardUrl("/login-error")
                .and()

                // logout settings
                .logout()
                    // link for logout
                    .logoutUrl("/logout").permitAll()
                    // redirect after logout
                    .logoutSuccessUrl("/");
    }



    /**
     * Configures authentication
     * @param auth builder for authentication configuration
     * @since 1.0
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(getDaoAuthenticationProvider());
    }



    /**
     * Creates bean that configures and returns {@link DaoAuthenticationProvider}
     * @return authentication provider
     * @since 1.0
     */
    @Bean
    public DaoAuthenticationProvider getDaoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

}
