package com.stasroshchenko.diploma.registration;

import com.stasroshchenko.diploma.auth.ApplicationUser;
import com.stasroshchenko.diploma.auth.ApplicationUserService;
import org.springframework.stereotype.Service;

import static com.stasroshchenko.diploma.security.ApplicationUserRole.CLIENT;

@Service
public class RegistrationService {

    private final ApplicationUserService applicationUserService;
    private final EmailValidator emailValidator;

    public RegistrationService(ApplicationUserService applicationUserService, EmailValidator emailValidator) {
        this.applicationUserService = applicationUserService;
        this.emailValidator = emailValidator;
    }

    public String signUpUser(RegistrationRequest request) {
        String requestEmail = request.getEmail();
        boolean emailValid = emailValidator.test(requestEmail);
        if (!emailValid) {
            throw new IllegalStateException("Email " + requestEmail + " isn't valid");
        }
        return applicationUserService.signUpUser(
                new ApplicationUser(
                        CLIENT,
                        request.getUsername(),
                        request.getEmail(),
                        request.getPassword(),
                        true,
                        true,
                        true,
                        true
                )
        );
    }
}
