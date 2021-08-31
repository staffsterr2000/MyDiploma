package com.stasroshchenko.clinic.controller.profile;

import com.stasroshchenko.clinic.model.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Works with user's own profile page, all the requests at
 * "localhost:8080/profile" will be proceeded in this controller.
 * @author staffsterr2000
 * @version 1.0
 */
@Controller
@RequestMapping
@AllArgsConstructor
public class ProfileController {

    /**
     * Link to profile service
     */
    private final ProfileService profileService;



    /**
     * Gets profile view depending on whether it is doctor's or
     * client's profile.
     * @param authentication current user's authentication
     * @param model model of attributes
     * @return either doctor's or client's standard/error view
     * @since 1.0
     */
    @GetMapping("/profile")
    public String getProfileView(
            Authentication authentication,
            Model model) {

        return profileService.initiateProfileView(authentication, model);

    }

}
