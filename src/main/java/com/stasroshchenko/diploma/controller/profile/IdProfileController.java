package com.stasroshchenko.diploma.controller.profile;

import com.stasroshchenko.diploma.entity.database.user.ApplicationUser;
import com.stasroshchenko.diploma.entity.database.user.ApplicationUserClient;
import com.stasroshchenko.diploma.entity.database.user.ApplicationUserDoctor;
import com.stasroshchenko.diploma.service.ProfileService;
import com.stasroshchenko.diploma.service.user.ApplicationUserService;
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

@Controller
@RequestMapping("/id")
@AllArgsConstructor
public class IdProfileController {

    private final ApplicationUserService applicationUserService;
    private final ProfileService profileService;


    // standard views
    public String getClientIdProfileView(
            ApplicationUserClient requiredUser,
            Authentication authentication,
            Model model) {

        profileService.initiateClientIdProfile(requiredUser, authentication, model);
        return "client_id_profile";
    }

    public String getDoctorIdProfileView(
            ApplicationUserDoctor requiredUser,
            Authentication authentication,
            Model model) {

        profileService.initiateDoctorIdProfile(requiredUser, authentication, model);
        return "doctor_id_profile";
    }



    // error views
    public String getClientIdProfileErrorView(
            ApplicationUserClient requiredUser,
            Authentication authentication,
            Model model,
            BindingResult result) {

        profileService.initiateClientIdProfile(requiredUser, authentication, model);
        profileService.addObjectAndFieldErrorsIntoModel(model, result);

        return "client_id_profile";
    }

    public String getDoctorIdProfileErrorView(
            ApplicationUserDoctor requiredUser,
            Authentication authentication,
            Model model,
            BindingResult result) {

        profileService.initiateDoctorIdProfile(requiredUser, authentication, model);
        profileService.addObjectAndFieldErrorsIntoModel(model, result);

        return "doctor_id_profile";
    }



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

}
