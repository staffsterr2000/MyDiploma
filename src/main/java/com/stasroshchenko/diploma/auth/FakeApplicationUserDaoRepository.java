package com.stasroshchenko.diploma.auth;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

import static com.stasroshchenko.diploma.security.ApplicationUserRole.*;

@Repository("fake")
public class FakeApplicationUserDaoRepository implements ApplicationUserDao {


    private final PasswordEncoder passwordEncoder;

    @Autowired
    public FakeApplicationUserDaoRepository(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> findByUsername(String username) {
        return loadUsers().stream()
                .filter(user -> username.equals(user.getUsername()))
                .findFirst();
    }

    @Override
    public Optional<ApplicationUser> findByEmail(String email) {
        return loadUsers().stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst();
    }

    @Bean
    public Set<ApplicationUser> loadUsers() {
        return Sets.newHashSet(
                new ApplicationUser(
                        ADMIN,
                        "admin",
                        "admin@gmail.com",
                        passwordEncoder.encode("admin"),
                        true,
                        true,
                        true,
                        true
                ),

                new ApplicationUser(
                        CLIENT,
                        "client",
                        "client@gmail.com",
                        passwordEncoder.encode("client"),
                        true,
                        true,
                        true,
                        true
                )
        );
    }
}
