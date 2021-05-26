package com.stasroshchenko.diploma.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/redirect")
public class RedirectController {

    @GetMapping
    public String getRedirectView(Model model, Authentication authentication) {
        model.addAttribute("user",
                authentication.getPrincipal());

        return "redirect";
    }

}
