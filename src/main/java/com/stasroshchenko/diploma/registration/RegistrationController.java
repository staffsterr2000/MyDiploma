package com.stasroshchenko.diploma.registration;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public String signUpUser(@RequestBody RegistrationRequest request) {
        return registrationService.signUpUser(request);
    }

}
