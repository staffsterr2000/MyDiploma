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

import java.util.List;

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

        initiateClientProfile(authentication, model);

        for (ObjectError objectError : result.getGlobalErrors()) {
            model.addAttribute(objectError.getObjectName(), objectError);
        }

        for (FieldError fieldError : result.getFieldErrors()) {
            model.addAttribute(fieldError.getObjectName() + "_" + fieldError.getField() + "Error", fieldError);
        }

        return "client_profile";
    }

    public String getDoctorProfileErrorView(
            Authentication authentication,
            Model model,
            BindingResult result) {

        initiateDoctorProfile(authentication, model);

        for (ObjectError objectError : result.getGlobalErrors()) {
            model.addAttribute(objectError.getObjectName(), objectError);
        }

        for (FieldError fieldError : result.getFieldErrors()) {
            model.addAttribute(fieldError.getObjectName() + "_" + fieldError.getField() + "Error", fieldError);
        }

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
                        VisitStatus.CANCELLED_BY_DOCTOR, VisitStatus.CANCELLED_BY_CLIENT);
        model.addAttribute("allAcceptedVisits", allAcceptedVisits);

        if (model.getAttribute("acceptRequest") == null) {
            model.addAttribute("acceptRequest", new AcceptVisitRequest());
        }
        if (model.getAttribute("declineRequest") == null) {
            model.addAttribute("declineRequest", new DeclineVisitRequest());
        }
        if (model.getAttribute("passRequest") == null) {
            model.addAttribute("passRequest", new PassVisitRequest());
        }
        if (model.getAttribute("createRequest") == null) {
            model.addAttribute("createRequest", new CreateVisitRequest());
        }

    }

    private void initiateClientProfile(Authentication authentication,
                                      Model model) {

        ApplicationUserClient principal =
                (ApplicationUserClient) authentication.getPrincipal();
        model.addAttribute("principal", principal);

        List<Visit> allVisits = visitService
                .getAllVisitsByClientOrdered(principal.getClientData());
        model.addAttribute("allVisits", allVisits);

        List<Visit> allSentAndActiveVisits = visitService
                .getAllVisitsWithSomeStatusesOrdered(VisitStatus.SENT, VisitStatus.ACTIVE);
        model.addAttribute("allSentAndActiveVisits", allSentAndActiveVisits);

        List<DoctorData> allDoctors =
                personDataService.getAllDoctors();
        model.addAttribute("allDoctors", allDoctors);

        if (model.getAttribute("sendRequest") == null) {
            model.addAttribute("sendRequest", new SendVisitRequest());
        }
        if (model.getAttribute("cancelRequest") == null) {
            model.addAttribute("cancelRequest", new CancelVisitRequest());
        }

    }

}
