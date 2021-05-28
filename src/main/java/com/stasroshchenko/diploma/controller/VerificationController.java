package com.stasroshchenko.diploma.controller;

import com.stasroshchenko.diploma.entity.RegistrationRequest;
import com.stasroshchenko.diploma.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/verification")
@AllArgsConstructor
public class VerificationController {

    private final RegistrationService registrationService;

    @GetMapping
    public String getVerificationView(
            @ModelAttribute("redirectedRequest")
                    RegistrationRequest request) {

        return "verification";
    }

    @PostMapping("/resend")
    public String resendConfirmationToken(
            @ModelAttribute RegistrationRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {

        if(result.hasErrors()) {
            return "registration";
        }
        try {
            registrationService.resendConfirmationToken(request);
        } catch (IllegalStateException ex) {
            result.addError(new ObjectError("error", ex.getMessage()));
            return "registration";
        }

        redirectAttributes.addFlashAttribute("redirectedRequest", request);
        return "redirect:/verification";
    }

}
