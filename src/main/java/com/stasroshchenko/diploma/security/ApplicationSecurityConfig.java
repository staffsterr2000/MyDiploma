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

@AllArgsConstructor

@Configuration      // цей компонент конфігурацією

// включаємо веб захист
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // відключити csrf
                .csrf().disable()
                .authorizeRequests()

                // дозволити доступ до всіх нижче написаних ресурсів без автентифікації
                .antMatchers("/", "/registration/**", "/verification/**",
                        "/id/**", "/index", "/css/**", "/img/**", "/js/**").permitAll()

                // інші реквести мають бути автентифікованими
                .anyRequest().authenticated()
                .and()

                // налаштування логіну
                .formLogin()
                // посилання для логіну
                    .loginPage("/login").permitAll()
                    // переадресація після входу
                    .defaultSuccessUrl("/", true)
                    // переадресація після невдачі
                    .failureForwardUrl("/login-error")
                .and()

                // налаштування логауту
                .logout()
                    // посилання для логауту
                    .logoutUrl("/logout").permitAll()
                    // переадресація при виході
                    .logoutSuccessUrl("/");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(getDaoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider getDaoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

}
