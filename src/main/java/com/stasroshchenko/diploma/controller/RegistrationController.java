package com.stasroshchenko.diploma.controller;

import com.stasroshchenko.diploma.entity.RegistrationRequest;
import com.stasroshchenko.diploma.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @GetMapping
    public String getRegistrationView(Model model) {
        model.addAttribute("request", new RegistrationRequest());
        return "registration";
    }

    @PostMapping
    public String signUpUser(
            @Valid @ModelAttribute("request") RegistrationRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {

        if(result.hasErrors()) {
            return "registration";
        }
        try {
            registrationService.signUpUser(request);
        } catch (IllegalStateException ex) {
            result.addError(new ObjectError("error", ex.getMessage()));
            return "registration";
        }

        redirectAttributes.addFlashAttribute("redirectedRequest", request);
        return "redirect:/verification";
    }

    @GetMapping("/confirm")
    public String confirmToken(
            @RequestParam("token") String token) {

        registrationService.confirmToken(token);
        return "redirect:/login";
    }

}
