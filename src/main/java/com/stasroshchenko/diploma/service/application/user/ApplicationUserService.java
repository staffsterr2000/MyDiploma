package com.stasroshchenko.diploma.service.application.user;

import com.stasroshchenko.diploma.entity.RegistrationRequest;
import com.stasroshchenko.diploma.entity.database.ApplicationUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// applicationUserService delegator

@Service
@AllArgsConstructor
public class ApplicationUserService {

    private final ApplicationUserServiceReal applicationUserServiceReal;

    public ApplicationUser loadUserByUsernameOrEmail(String input) throws UsernameNotFoundException {
        return applicationUserServiceReal.loadUserByUsername(input);
    }

    public ApplicationUser loadUserByUsername(String username) {
        return applicationUserServiceReal.loadUserByOnlyUsername(username);
    }

    public ApplicationUser loadUserByEmail(String email) {
        return applicationUserServiceReal.loadUserByOnlyEmail(email);
    }

    public ApplicationUser loadUserByUser(ApplicationUser user) {
        return applicationUserServiceReal.loadUserByUser(user);
    }

    public void enableUserByEmail(String email) {
        applicationUserServiceReal.enableUserByEmail(email);
    }

    public String signUpUser(ApplicationUser rawUser) {
        return applicationUserServiceReal.signUpUser(rawUser);
    }

    public String resendConfirmationToken(ApplicationUser user) {
        return applicationUserServiceReal.resendConfirmationToken(user);
    }
}
