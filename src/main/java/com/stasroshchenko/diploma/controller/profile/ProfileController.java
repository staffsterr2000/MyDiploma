package com.stasroshchenko.diploma.controller.profile;

import com.stasroshchenko.diploma.entity.request.visit.*;
import com.stasroshchenko.diploma.entity.database.Visit;
import com.stasroshchenko.diploma.entity.database.person.DoctorData;
import com.stasroshchenko.diploma.entity.database.user.ApplicationUserClient;
import com.stasroshchenko.diploma.entity.database.user.ApplicationUserDoctor;
import com.stasroshchenko.diploma.service.PersonDataService;
import com.stasroshchenko.diploma.service.VisitService;
import com.stasroshchenko.diploma.util.VisitStatus;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping
@AllArgsConstructor
public class ProfileController {

    private final VisitService visitService;
    private final PersonDataService personDataService;

    @GetMapping("/profile")
    public String getProfileView(
            Authentication authentication,
            Model model) {

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

    public String getClientProfileView(Authentication authentication, Model model) {
        initiateClientProfile(authentication, model);

        return "client_profile";
    }

    public String getDoctorProfileView(Authentication authentication, Model model) {
        initiateDoctorProfile(authentication, model);

        return "doctor_profile";
    }

    public String getClientProfileErrorView(
            Authentication authentication,
            Model model,
            BindingResult result) {

        for (ObjectError objectError : result.getGlobalErrors()) {
            model.addAttribute(objectError.getObjectName(), objectError);
        }

        for (FieldError fieldError : result.getFieldErrors()) {
            model.addAttribute(fieldError.getObjectName() + "_" + fieldError.getField() + "Error", fieldError);
        }

        initiateClientProfile(authentication, model);

        return "client_profile";
    }

    public String getDoctorProfileErrorView(
            Authentication authentication,
            Model model,
            BindingResult result) {

        for (ObjectError objectError : result.getGlobalErrors()) {
            model.addAttribute(objectError.getObjectName(), objectError);
        }

        for (FieldError fieldError : result.getFieldErrors()) {
            model.addAttribute(fieldError.getObjectName() + "_" + fieldError.getField() + "Error", fieldError);
        }

        initiateDoctorProfile(authentication, model);

        return "doctor_profile";
    }

    private void initiateDoctorProfile(Authentication authentication,
                                      Model model) {

        ApplicationUserDoctor principal =
                (ApplicationUserDoctor) authentication.getPrincipal();
        model.addAttribute("principal", principal);

        List<Visit> allSentVisits = visitService
                .getAllVisitsWithSomeStatusesOrdered(VisitStatus.SENT);
        model.addAttribute("allSentVisits", allSentVisits);

        List<Visit> allActiveVisits = visitService
                .getAllVisitsWithSomeStatusesOrdered(VisitStatus.ACTIVE);
        model.addAttribute("allActiveVisits", allActiveVisits);

        List<Visit> allAcceptedVisits = visitService
                .getAllVisitsExceptVisitsWithSomeStatusesOrdered(VisitStatus.SENT,
                        VisitStatus.CANCELLED);
        model.addAttribute("allAcceptedVisits", allAcceptedVisits);

        model.addAttribute("acceptRequest", new AcceptVisitRequest());
        model.addAttribute("declineRequest", new DeclineVisitRequest());
        model.addAttribute("passRequest", new PassVisitRequest());
        model.addAttribute("createRequest", new CreateVisitRequest());

    }

    private void initiateClientProfile(Authentication authentication,
                                      Model model) {

        ApplicationUserClient principal =
                (ApplicationUserClient) authentication.getPrincipal();
        model.addAttribute("principal", principal);

        List<Visit> allVisits = visitService
                .getAllVisitsByClientOrdered(principal.getClientData());
        model.addAttribute("allVisits", allVisits);

        List<DoctorData> allDoctors =
                personDataService.getAllDoctors();
        model.addAttribute("allDoctors", allDoctors);

        model.addAttribute("sendRequest", new SendVisitRequest());
    }

}
