package com.stasroshchenko.diploma.controller;

import com.stasroshchenko.diploma.registration.RegistrationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class TemplateController {

    @GetMapping("/login")
    public String getLoginView() {
        return "login";
    }

    @GetMapping("/redirect")
    public String getRedirectView() {
        return "redirect";
    }

    @GetMapping("/registration")
    public String getRegistrationView(@ModelAttribute RegistrationRequest request,
                                      Model model) {
        model.addAttribute("request", request);
        return "registration";
    }

}
