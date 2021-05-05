package com.stasroshchenko.diploma.auth;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

import static com.stasroshchenko.diploma.security.UserRole.*;

@Repository("fake")
public class FakeApplicationUserDaoRepository implements ApplicationUserDao {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public FakeApplicationUserDaoRepository(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return loadUsers().stream()
                .filter(user -> username.equals(user.getUsername()))
                .findFirst();
    }

    @Bean
    public Set<ApplicationUser> loadUsers() {
        return Sets.newHashSet(
                new ApplicationUser(
                        ADMIN.getGrantedAuthorities(),
                        "admin",
                        passwordEncoder.encode("admin"),
                        true,
                        true,
                        true,
                        true
                ),

                new ApplicationUser(
                        CLIENT.getGrantedAuthorities(),
                        "client",
                        passwordEncoder.encode("client"),
                        true,
                        true,
                        true,
                        true
                )
        );
    }
}
