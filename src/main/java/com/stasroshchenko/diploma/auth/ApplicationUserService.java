package com.stasroshchenko.diploma.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService implements UserDetailsService {

    private final ApplicationUserDaoJpa applicationUserDaoJpa;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationUserService(@Qualifier("jpa") ApplicationUserDaoJpa applicationUserDaoJpa,
                                  PasswordEncoder passwordEncoder) {
        this.applicationUserDaoJpa = applicationUserDaoJpa;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ApplicationUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return applicationUserDaoJpa.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
    }

    public String signUpUser(ApplicationUser user) {
        String userUsername = user.getUsername();
        String userEmail = user.getEmail();

        boolean userByUsernameIsPresent =
                applicationUserDaoJpa.findByUsername(userUsername)
                .isPresent();
        if (userByUsernameIsPresent) {
            throw new IllegalStateException(String.format("Username %s has been already taken", userUsername));
        }

        boolean userByEmailIsPresent =
                applicationUserDaoJpa.findByEmail(userEmail)
                        .isPresent();
        if (userByEmailIsPresent) {
            throw new IllegalStateException(String.format("Email %s has been already taken", userEmail));
        }

        String encodedPassword = passwordEncoder
                .encode(user.getPassword());
        user.setPassword(encodedPassword);

        applicationUserDaoJpa.save(user);

        return "It works!";
    }

}
