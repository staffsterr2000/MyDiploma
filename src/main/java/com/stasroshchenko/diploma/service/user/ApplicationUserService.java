package com.stasroshchenko.diploma.service.user;

import com.stasroshchenko.diploma.entity.database.ApplicationUser;
import com.stasroshchenko.diploma.entity.database.person.PersonData;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// applicationUserService delegator

@Service
@AllArgsConstructor
public class ApplicationUserService {

    private final ApplicationUserServiceReal applicationUserServiceReal;

    public ApplicationUser getByUsernameOrEmail(String input) throws UsernameNotFoundException {
        return applicationUserServiceReal.loadUserByUsername(input);
    }

    public ApplicationUser getByUsername(String username) {
        return applicationUserServiceReal.loadUserByOnlyUsername(username);
    }

    public ApplicationUser getByEmail(String email) {
        return applicationUserServiceReal.loadUserByOnlyEmail(email);
    }

    public ApplicationUser getByUser(ApplicationUser user) {
        return applicationUserServiceReal.loadUserByUser(user);
    }

    public ApplicationUser getByPersonData(PersonData personData) {
        return applicationUserServiceReal.loadUserByPersonData(personData);
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
