package com.stasroshchenko.diploma.controller.profile;

import com.stasroshchenko.diploma.model.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
     * @param authentication Current user's authentication
     * @param model Model of attributes
     * @return either doctor's or client's standard/error view
     * @since 1.0
     */
    @GetMapping("/profile")
    public String getProfileView(
            Authentication authentication,
            Model model) {

        model.addAttribute("redirectLink", "profile");

        BindingResult bindingResult = (BindingResult) model.getAttribute("bindingResult");
        boolean error = bindingResult != null;

        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            switch (grantedAuthority.getAuthority()) {
                case "ROLE_CLIENT":
                    return (!error) ? getClientProfileView(authentication, model) :
                            getClientProfileErrorView(authentication, model, bindingResult);
                case "ROLE_DOCTOR":
                    return (!error) ? getDoctorProfileView(authentication, model) :
                            getDoctorProfileErrorView(authentication, model, bindingResult);
            }
        }

        throw new IllegalStateException("Unknown role");
    }



    /**
     * Redirects to the service model and authentication for further initialization.
     * @param authentication Current user's authentication.
     * @param model Model of attributes
     * @return Client view
     * @since 1.0
     * @see ProfileService
     */
    private String getClientProfileView(
            Authentication authentication,
            Model model) {

        profileService.initiateClientProfile(authentication, model);

        return "client_profile";
    }



    /**
     * Redirects to the service model and authentication for further initialization.
     * @param authentication Current user's authentication.
     * @param model Model of attributes
     * @return Doctor view
     * @since 1.0
     * @see ProfileService
     */
    private String getDoctorProfileView(
            Authentication authentication,
            Model model) {

        profileService.initiateDoctorProfile(authentication, model);

        return "doctor_profile";
    }



    /**
     * Redirects to the service model, authentication and bindingResult for
     * further initialization.
     * @param authentication Current user's authentication
     * @param model Model of attributes
     * @param result Result of validation and binding (contains errors)
     * @return Client view
     * @since 1.0
     * @see ProfileService
     */
    private String getClientProfileErrorView(
            Authentication authentication,
            Model model,
            BindingResult result) {

        profileService.initiateClientProfile(authentication, model);
        profileService.addErrorsIntoModel(model, result);

        return "client_profile";
    }



    /**
     * Redirects to the service model, authentication and bindingResult for
     * further initialization.
     * @param authentication Current user's authentication
     * @param model Model of attributes
     * @param result Result of validation and binding (contains errors)
     * @return Doctor view
     * @since 1.0
     * @see ProfileService
     */
    private String getDoctorProfileErrorView(
            Authentication authentication,
            Model model,
            BindingResult result) {

        profileService.initiateDoctorProfile(authentication, model);
        profileService.addErrorsIntoModel(model, result);

        return "doctor_profile";
    }

}
