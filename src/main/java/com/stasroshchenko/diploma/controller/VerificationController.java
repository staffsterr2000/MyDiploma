package com.stasroshchenko.diploma.controller;

import com.stasroshchenko.diploma.entity.RegistrationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/verification")
public class VerificationController {

    @GetMapping
    public String getVerificationView(
            @Valid @ModelAttribute("redirectedRequest")
                    RegistrationRequest request) {

        return "verification";
    }

}
