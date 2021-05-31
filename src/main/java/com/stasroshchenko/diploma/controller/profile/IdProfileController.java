package com.stasroshchenko.diploma.controller.profile;

import com.stasroshchenko.diploma.entity.database.Visit;
import com.stasroshchenko.diploma.entity.database.user.ApplicationUser;
import com.stasroshchenko.diploma.entity.database.user.ApplicationUserClient;
import com.stasroshchenko.diploma.entity.database.user.ApplicationUserDoctor;
import com.stasroshchenko.diploma.service.user.ApplicationUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/id")
@AllArgsConstructor
public class IdProfileController {

    private final ApplicationUserService applicationUserService;

    @GetMapping("/{username}")
    @PreAuthorize("isAuthenticated()")
    public String getIdProfileView(
            @PathVariable("username") String username,
            Model model) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        ApplicationUser authenticatedUser =
                (ApplicationUser) authentication.getPrincipal();

        model.addAttribute("auth", authentication);

        model.addAttribute("isAuthUserClient",
                authenticatedUser instanceof ApplicationUserClient);

        ApplicationUser userByUsername =
                applicationUserService.getByUsername(username);

        model.addAttribute("requiredUser", userByUsername);

        if (userByUsername instanceof ApplicationUserDoctor) {
            model.addAttribute("visitToSend", new Visit());
            return "doctor_id_profile";
        }

        return "default_id_profile";
    }

}
