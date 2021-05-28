package com.stasroshchenko.diploma.controller;

import com.stasroshchenko.diploma.entity.database.ApplicationUser;
import com.stasroshchenko.diploma.service.PersonDataService;
import com.stasroshchenko.diploma.service.user.ApplicationUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class HomeController {

    private final PersonDataService personDataService;
    private final ApplicationUserService applicationUserService;

    @ModelAttribute("allDoctorUsers")
    public List<ApplicationUser> getAllDoctorUsers() {
        return personDataService.getAllDoctors().stream()
                .map(applicationUserService::getByPersonData)
                .collect(Collectors.toList());
    }

    @GetMapping
    public String getIndexView(Model model) {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        model.addAttribute("auth", authentication);

        return "index";
    }

}
