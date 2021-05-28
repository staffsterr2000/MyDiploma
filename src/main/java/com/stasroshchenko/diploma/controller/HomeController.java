package com.stasroshchenko.diploma.controller;

import com.stasroshchenko.diploma.entity.database.person.DoctorData;
import com.stasroshchenko.diploma.service.PersonDataService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@AllArgsConstructor
public class HomeController {

    private final PersonDataService personDataService;

    @ModelAttribute("allDoctors")
    public List<DoctorData> getAllDoctors() {
        return personDataService.getAllDoctors();
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
