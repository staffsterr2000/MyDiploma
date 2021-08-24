package com.stasroshchenko.diploma.controller.profile;

import com.stasroshchenko.diploma.entity.user.ApplicationUser;
import com.stasroshchenko.diploma.entity.user.ApplicationUserClient;
import com.stasroshchenko.diploma.entity.user.ApplicationUserDoctor;
import com.stasroshchenko.diploma.model.service.ProfileService;
import com.stasroshchenko.diploma.model.service.user.ApplicationUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
     * Connection with user service
     */
    private final ApplicationUserService applicationUserService;

    /**
     * Connection with profile service
     */
    private final ProfileService profileService;



    /**
     * Gets profile view depending on whether current user is in a doctor's
     * profile or client's profile. Current user must be authenticated.
     * @param username Username of the user, page of whom the current user
     *                 has attended
     * @param authentication Current user's authentication
     * @param model Model of attributes
     * @return Either doctor's or client's standard/error view
     * @since 1.0
     * @see ApplicationUserService
     */
    @GetMapping("/{username}")
    @PreAuthorize("isAuthenticated()")
    public String getIdProfileView(
            @PathVariable("username") String username,
            Authentication authentication,
            Model model) {

        model.addAttribute("redirectLink", "id/" + username);

        BindingResult bindingResult = (BindingResult) model.getAttribute("bindingResult");
        boolean error = bindingResult != null;

        ApplicationUser requiredUser = applicationUserService
                .getByUsername(username);
        model.addAttribute("requiredUser", requiredUser);

        for (GrantedAuthority grantedAuthority : requiredUser.getAuthorities()) {
            switch (grantedAuthority.getAuthority()) {
                case "ROLE_CLIENT":
                    return (!error) ? getClientIdProfileView((ApplicationUserClient) requiredUser, authentication, model) :
                            getClientIdProfileErrorView((ApplicationUserClient) requiredUser, authentication, model, bindingResult);
                case "ROLE_DOCTOR":
                    return (!error) ? getDoctorIdProfileView((ApplicationUserDoctor) requiredUser, authentication, model) :
                            getDoctorIdProfileErrorView((ApplicationUserDoctor) requiredUser, authentication, model, bindingResult);
            }
        }

        throw new IllegalStateException("Unknown role");

    }



    /**
     * Redirects params to the service for further initialization.
     * @param requiredUser User whose page the current user has attended
     * @param authentication Current user's authentication
     * @param model Model of attributes
     * @return Client's view
     * @since 1.0
     * @see ProfileService
     */
    public String getClientIdProfileView(
            ApplicationUserClient requiredUser,
            Authentication authentication,
            Model model) {

        profileService.initiateClientIdProfile(requiredUser, authentication, model);

        return "client_id_profile";
    }



    /**
     * Redirects params to the service for further initialization.
     * @param requiredUser User whose page the current user has attended
     * @param authentication Current user's authentication
     * @param model Model of attributes
     * @return Doctor's view
     * @since 1.0
     * @see ProfileService
     */
    public String getDoctorIdProfileView(
            ApplicationUserDoctor requiredUser,
            Authentication authentication,
            Model model) {

        profileService.initiateDoctorIdProfile(requiredUser, authentication, model);

        return "doctor_id_profile";
    }



    /**
     * Redirects params to the service for further initialization.
     * @param requiredUser User whose page the current user has attended
     * @param authentication Current user's authentication
     * @param model Model of attributes
     * @param result Result of validation and binding (contains errors)
     * @return Client's view
     * @since 1.0
     * @see ProfileService
     */
    public String getClientIdProfileErrorView(
            ApplicationUserClient requiredUser,
            Authentication authentication,
            Model model,
            BindingResult result) {

        profileService.initiateClientIdProfile(requiredUser, authentication, model);
        profileService.addErrorsIntoModel(model, result);

        return "client_id_profile";
    }



    /**
     * Redirects params to the service for further initialization.
     * @param requiredUser User whose page the current user has attended
     * @param authentication Current user's authentication
     * @param model Model of attributes
     * @param result Result of validation and binding (contains errors)
     * @return Doctor's view
     * @since 1.0
     * @see ProfileService
     */
    public String getDoctorIdProfileErrorView(
            ApplicationUserDoctor requiredUser,
            Authentication authentication,
            Model model,
            BindingResult result) {

        profileService.initiateDoctorIdProfile(requiredUser, authentication, model);
        profileService.addErrorsIntoModel(model, result);

        return "doctor_id_profile";
    }

}
