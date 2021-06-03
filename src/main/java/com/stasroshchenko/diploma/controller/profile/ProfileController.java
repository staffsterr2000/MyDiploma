package com.stasroshchenko.diploma.controller.profile;

import com.stasroshchenko.diploma.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@AllArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

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

    public String getClientProfileView(
            Authentication authentication,
            Model model) {

        profileService.initiateClientProfile(authentication, model);

        return "client_profile";
    }

    public String getDoctorProfileView(
            Authentication authentication,
            Model model) {

        profileService.initiateDoctorProfile(authentication, model);

        return "doctor_profile";
    }

    public String getClientProfileErrorView(
            Authentication authentication,
            Model model,
            BindingResult result) {

        profileService.initiateClientProfile(authentication, model);
        profileService.addObjectAndFieldErrorsIntoModel(model, result);

        return "client_profile";
    }

    public String getDoctorProfileErrorView(
            Authentication authentication,
            Model model,
            BindingResult result) {

        profileService.initiateDoctorProfile(authentication, model);
        profileService.addObjectAndFieldErrorsIntoModel(model, result);

        return "doctor_profile";
    }

}
