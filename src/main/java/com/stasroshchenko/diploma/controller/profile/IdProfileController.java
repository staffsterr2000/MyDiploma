package com.stasroshchenko.diploma.controller.profile;

import com.stasroshchenko.diploma.model.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Works with another users' profile pages, all the requests at
 * "localhost:8080/id/{username-of-another-user}" will be proceeded in this controller.
 * @author staffsterr2000
 * @version 1.0
 */
@Controller
@RequestMapping("/id")
@AllArgsConstructor
public class IdProfileController {

    /**
     * Connection with profile service
     */
    private final ProfileService profileService;



    /**
     * Gets profile view depending on whether required user is a doctor
     * user or a client user. Current user must be authenticated.
     * @param username username of the user, page of whom the current user
     *                 has attended
     * @param authentication current user's authentication
     * @param model model of attributes
     * @return either doctor's or client's standard/error view
     * @since 1.0
     */
    @GetMapping("/{username}")
    @PreAuthorize("isAuthenticated()")
    public String getIdProfileView(
            @PathVariable("username") String username,
            Authentication authentication,
            Model model) {

        return profileService.initiateIdProfileView(username, authentication, model);

    }

}
