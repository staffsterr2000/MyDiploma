package com.stasroshchenko.diploma.controller.profile;

import com.stasroshchenko.diploma.entity.database.ApplicationUser;
import com.stasroshchenko.diploma.entity.database.person.ClientData;
import com.stasroshchenko.diploma.entity.database.person.DoctorData;
import com.stasroshchenko.diploma.service.user.ApplicationUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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
    public String getIdProfileView(
            @PathVariable("username") String username,
            Model model) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        model.addAttribute("auth", authentication);

        boolean isAnonymous = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ANONYMOUS"));

        try {
            ApplicationUser userByUsername =
                    applicationUserService.getByUsername(username);

            if (isAnonymous &&
                    !(userByUsername.getPersonData() instanceof DoctorData)) {

                throw new AccessDeniedException("Forbidden");
            }

            model.addAttribute("userRole", userByUsername.getPersonData().getClass().getSimpleName());
            model.addAttribute("requiredUser", userByUsername);

        } catch (IllegalStateException ex) {
            if (isAnonymous) {
                throw new AccessDeniedException("Forbidden");
            }
            throw ex;
        }

        return "id_profile";
    }

}
