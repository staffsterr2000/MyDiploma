package com.stasroshchenko.clinic.controller;

import com.stasroshchenko.clinic.entity.user.ApplicationUserDoctor;
import com.stasroshchenko.clinic.model.service.user.ApplicationUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * The controller works with Home page, all the requests
 * by "localhost:8080/" come into this controller.
 * @author staffsterr2000
 * @version 1.0
 */
@Controller
@RequestMapping("/")
@AllArgsConstructor
public class HomeController {

    /**
     * User service
     */
    private final ApplicationUserService applicationUserService;



    /**
     * Returns list of all doctor users to display them in the Home page
     * Adds this list to Model.
     * @return list of all doctor users
     * @since 1.0
     * @see ApplicationUserService
     * @see ApplicationUserDoctor
     */
    @ModelAttribute("allDoctorUsers")
    public List<ApplicationUserDoctor> getAllDoctorUsers() {
        return applicationUserService.getAllUserDoctors();
    }



    /**
     * Works on GET method of incoming request.
     * Adds to Model current user's authentication.
     * @param model model of attributes
     * @return view name
     * @since 1.0
     */
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
