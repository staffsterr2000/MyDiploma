package com.stasroshchenko.diploma.model.service.user;

import com.stasroshchenko.diploma.entity.person.ClientData;
import com.stasroshchenko.diploma.entity.person.DoctorData;
import com.stasroshchenko.diploma.entity.user.ApplicationUser;
import com.stasroshchenko.diploma.entity.user.ApplicationUserClient;
import com.stasroshchenko.diploma.entity.user.ApplicationUserDoctor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public ApplicationUserClient getByClientData(ClientData clientData) {
        return applicationUserServiceReal.loadUserByClientData(clientData);
    }

    public ApplicationUserDoctor getByDoctorData(DoctorData doctorData) {
        return applicationUserServiceReal.loadUserByDoctorData(doctorData);
    }

    public List<ApplicationUserClient> getAllUserClients() {
        return applicationUserServiceReal.loadAllUserClients();
    }

    public List<ApplicationUserDoctor> getAllUserDoctors() {
        return applicationUserServiceReal.loadAllUserDoctors();
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
