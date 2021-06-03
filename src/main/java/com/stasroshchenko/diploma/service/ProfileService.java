package com.stasroshchenko.diploma.service;

import com.stasroshchenko.diploma.entity.database.Visit;
import com.stasroshchenko.diploma.entity.database.person.ClientData;
import com.stasroshchenko.diploma.entity.database.person.DoctorData;
import com.stasroshchenko.diploma.entity.database.user.ApplicationUser;
import com.stasroshchenko.diploma.entity.database.user.ApplicationUserClient;
import com.stasroshchenko.diploma.entity.database.user.ApplicationUserDoctor;
import com.stasroshchenko.diploma.entity.request.visit.*;
import com.stasroshchenko.diploma.util.VisitStatus;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProfileService {

    private final VisitService visitService;
    private final PersonDataService personDataService;

    public List<Visit> remainOnlyOneClientDataVisitsInList(
            ClientData clientData,
            List<Visit> source) {

        return source.stream()
                .filter(visit -> visit.getClientData()
                        .equals(clientData))
                .collect(Collectors.toList());
    }

    public List<Visit> remainOnlyOneDoctorDataVisitsInList(
            DoctorData doctorData,
            List<Visit> source) {

        return source.stream()
                .filter(visit -> visit.getDoctorData()
                        .equals(doctorData))
                .collect(Collectors.toList());
    }




    public void addObjectAndFieldErrorsIntoModel(
            Model model,
            BindingResult result) {

        for (ObjectError objectError : result.getGlobalErrors()) {
            model.addAttribute(objectError.getObjectName(), objectError);
        }

        for (FieldError fieldError : result.getFieldErrors()) {
            model.addAttribute(fieldError.getObjectName() + "_" + fieldError.getField() + "Error", fieldError);
        }

    }




    // initialize
    public void initiateClientIdProfile(
            ApplicationUserClient requiredUser,
            Authentication authentication,
            Model model) {

        ApplicationUser authenticatedUser =
                (ApplicationUser) authentication.getPrincipal();

        if (authenticatedUser instanceof ApplicationUserDoctor) {
            ApplicationUserDoctor authenticatedDoctorUser =
                    (ApplicationUserDoctor) authenticatedUser;

            model.addAttribute("isAuthUserDoctor", true);

            List<Visit> allVisits = visitService
                    .getAllVisitsByDoctorOrdered(authenticatedDoctorUser.getDoctorData());
            allVisits = remainOnlyOneClientDataVisitsInList(requiredUser.getClientData(), allVisits);
            model.addAttribute("allVisits", allVisits);

            List<Visit> allSentVisits = visitService
                    .getAllVisitsWithSomeStatusesOrdered(VisitStatus.SENT);
            allSentVisits = remainOnlyOneClientDataVisitsInList(requiredUser.getClientData(), allSentVisits);
            model.addAttribute("allSentVisits", allSentVisits);

            List<Visit> allActiveVisits = visitService
                    .getAllVisitsWithSomeStatusesOrdered(VisitStatus.ACTIVE);
            allActiveVisits = remainOnlyOneClientDataVisitsInList(requiredUser.getClientData(), allActiveVisits);
            model.addAttribute("allActiveVisits", allActiveVisits);

            if (model.getAttribute("acceptRequest") == null) {
                model.addAttribute("acceptRequest", new AcceptVisitRequest());
            }
            if (model.getAttribute("declineRequest") == null) {
                model.addAttribute("declineRequest", new DeclineVisitRequest());
            }
            if (model.getAttribute("passRequest") == null) {
                model.addAttribute("passRequest", new PassVisitRequest());
            }

        }
    }

    public void initiateDoctorIdProfile(
            ApplicationUserDoctor requiredUser,
            Authentication authentication,
            Model model) {

        ApplicationUser authenticatedUser =
                (ApplicationUser) authentication.getPrincipal();

        if (authenticatedUser instanceof ApplicationUserClient) {
            ApplicationUserClient authenticatedClientUser =
                    (ApplicationUserClient) authenticatedUser;

            model.addAttribute("isAuthUserClient", true);

            List<Visit> allVisits = visitService
                    .getAllVisitsByClientOrdered(authenticatedClientUser.getClientData());
            allVisits = remainOnlyOneDoctorDataVisitsInList(requiredUser.getDoctorData(), allVisits);
            model.addAttribute("allVisits", allVisits);

            List<Visit> allSentAndActiveVisits = visitService
                    .getAllVisitsWithSomeStatusesOrdered(VisitStatus.SENT, VisitStatus.ACTIVE);
            allSentAndActiveVisits = remainOnlyOneDoctorDataVisitsInList(requiredUser.getDoctorData(), allSentAndActiveVisits);
            model.addAttribute("allSentAndActiveVisits", allSentAndActiveVisits);

            if (model.getAttribute("sendRequest") == null) {
                model.addAttribute("sendRequest", new SendVisitRequest());
            }
            if (model.getAttribute("cancelRequest") == null) {
                model.addAttribute("cancelRequest", new CancelVisitRequest());
            }
        }

    }

    public void initiateDoctorProfile(
            Authentication authentication,
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

    public void initiateClientProfile(
            Authentication authentication,
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
