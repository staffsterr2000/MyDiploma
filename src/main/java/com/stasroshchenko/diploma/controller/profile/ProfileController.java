package com.stasroshchenko.diploma.controller.profile;

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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            switch (grantedAuthority.getAuthority()) {
                case "ROLE_CLIENT":
                    return getClientProfileView(authentication, model);
                case "ROLE_DOCTOR":
                    return getDoctorProfileView(authentication, model);
            }
        }

        throw new IllegalStateException("Unknown role");
    }

    public String getClientProfileView(Authentication authentication, Model model) {
        ApplicationUserClient principal =
                (ApplicationUserClient) authentication.getPrincipal();
        model.addAttribute("principal", principal);

        List<Visit> allVisits = visitService
                .getAllVisitsByClientOrdered(principal.getClientData());
        model.addAttribute("allVisits", allVisits);

        List<DoctorData> allDoctors =
                personDataService.getAllDoctors();
        model.addAttribute("allDoctors", allDoctors);

        model.addAttribute("visitToSend", new Visit());
        return "client_profile";
    }

    public String getDoctorProfileView(Authentication authentication, Model model) {
        ApplicationUserDoctor principal =
                (ApplicationUserDoctor) authentication.getPrincipal();
        model.addAttribute("principal", principal);

        List<Visit> allVisits = visitService
                .getAllVisitsByDoctorOrdered(principal.getDoctorData());
        List<Visit> allVisitsExceptCancelled = allVisits.stream()
                .filter(visit -> !visit.getStatus().equals(VisitStatus.CANCELLED))
                .collect(Collectors.toList());

        List<Visit> allSentVisits = allVisits.stream()
                .filter(visit -> visit.getStatus().equals(VisitStatus.SENT))
                .collect(Collectors.toList());
        model.addAttribute("allSentVisits", allSentVisits);

        List<Visit> allActiveVisits = allVisits.stream()
                .filter(visit -> visit.getStatus().equals(VisitStatus.ACTIVE))
                .collect(Collectors.toList());
        model.addAttribute("allActiveVisits", allActiveVisits);

        List<Visit> alreadyAcceptedVisits = new ArrayList<>(allVisitsExceptCancelled);
        alreadyAcceptedVisits.removeAll(allSentVisits);
        model.addAttribute("allAcceptedVisits", alreadyAcceptedVisits);

        model.addAttribute("visitToManage", new Visit());

        return "doctor_profile";
    }

    @GetMapping("/profile-error")
    public String getProfileErrorView(
            Authentication authentication,
            Model model,
            @ModelAttribute("bindingResult") BindingResult bindingResult) {

        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            switch (grantedAuthority.getAuthority()) {
                case "ROLE_CLIENT":
                    return getClientProfileErrorView(authentication, model, bindingResult);
                case "ROLE_DOCTOR":
                    return getDoctorProfileErrorView(authentication, model, bindingResult);
            }
        }

        throw new IllegalStateException("Unknown role");
    }

    public String getClientProfileErrorView(
            Authentication authentication,
            Model model,
            BindingResult result) {

        for (ObjectError objectError : result.getGlobalErrors()) {
            model.addAttribute(objectError.getObjectName(), objectError);
        }

        ApplicationUserClient principal =
                (ApplicationUserClient) authentication.getPrincipal();
        model.addAttribute("principal", principal);

        List<Visit> allVisits = visitService
                .getAllVisitsByClientOrdered(principal.getClientData());
        model.addAttribute("allVisits", allVisits);

        List<DoctorData> allDoctors =
                personDataService.getAllDoctors();
        model.addAttribute("allDoctors", allDoctors);

        model.addAttribute("visitToSend", new Visit());
        return "client_profile";
    }

    public String getDoctorProfileErrorView(
            Authentication authentication,
            Model model,
            BindingResult result) {

        for (ObjectError objectError : result.getGlobalErrors()) {
            model.addAttribute(objectError.getObjectName(), objectError);
        }

        ApplicationUserDoctor principal =
                (ApplicationUserDoctor) authentication.getPrincipal();
        model.addAttribute("principal", principal);

        List<Visit> allVisits = visitService
                .getAllVisitsByDoctorOrdered(principal.getDoctorData());
        List<Visit> allVisitsExceptCancelled = allVisits.stream()
                .filter(visit -> !visit.getStatus().equals(VisitStatus.CANCELLED))
                .collect(Collectors.toList());

        List<Visit> allSentVisits = allVisits.stream()
                .filter(visit -> visit.getStatus().equals(VisitStatus.SENT))
                .collect(Collectors.toList());
        model.addAttribute("allSentVisits", allSentVisits);

        List<Visit> alreadyAcceptedVisits = new ArrayList<>(allVisitsExceptCancelled);
        alreadyAcceptedVisits.removeAll(allSentVisits);
        model.addAttribute("allAcceptedVisits", alreadyAcceptedVisits);

        model.addAttribute("visitToManage", new Visit());

        return "doctor_profile";
    }

}
