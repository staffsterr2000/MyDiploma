package com.stasroshchenko.diploma.model.service;

import com.stasroshchenko.diploma.entity.Visit;
import com.stasroshchenko.diploma.entity.person.ClientData;
import com.stasroshchenko.diploma.entity.person.DoctorData;
import com.stasroshchenko.diploma.entity.user.ApplicationUser;
import com.stasroshchenko.diploma.entity.user.ApplicationUserClient;
import com.stasroshchenko.diploma.entity.user.ApplicationUserDoctor;
import com.stasroshchenko.diploma.request.visit.*;
import com.stasroshchenko.diploma.model.service.user.ApplicationUserService;
import com.stasroshchenko.diploma.util.VisitStatus;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProfileService {

    private final static Logger LOGGER =
            LoggerFactory.getLogger(ProfileService.class);

    private final VisitService visitService;
    private final PersonDataService personDataService;
    private final ApplicationUserService applicationUserService;

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

    public Map<Visit, ApplicationUserClient> addClientUsersFromVisitToAllDoctorVisits(List<Visit> visits) {
        Map<Visit, ApplicationUserClient>
                allVisitsByDoctorWithItsClientUsers = new LinkedHashMap<>();

        for (Visit visit : visits) {
            ClientData clientData = visit.getClientData();
            ApplicationUserClient applicationUserClient;
            try {
                applicationUserClient = applicationUserService.getByClientData(clientData);

            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
                applicationUserClient = null;
            }

            allVisitsByDoctorWithItsClientUsers.put(visit, applicationUserClient);
        }

        return allVisitsByDoctorWithItsClientUsers;
    }

    public Map<Visit, ApplicationUserDoctor> addDoctorUsersFromVisitToAllClientVisits(List<Visit> visits) {
        Map<Visit, ApplicationUserDoctor>
                allVisitsByClientWithItsDoctorUsers = new LinkedHashMap<>();

        for (Visit visit : visits) {
            DoctorData doctorData = visit.getDoctorData();
            ApplicationUserDoctor applicationUserDoctor;
            try {
                applicationUserDoctor = applicationUserService.getByDoctorData(doctorData);
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
                applicationUserDoctor = null;
            }

            allVisitsByClientWithItsDoctorUsers.put(visit, applicationUserDoctor);
        }

        return allVisitsByClientWithItsDoctorUsers;
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
            DoctorData doctorUser = authenticatedDoctorUser.getDoctorData();

            model.addAttribute("isAuthUserDoctor", true);

            List<Visit> allVisits = visitService
                    .getAllVisitsByDoctor(doctorUser);
            allVisits = remainOnlyOneClientDataVisitsInList(requiredUser.getClientData(), allVisits);
            model.addAttribute("allVisits", allVisits);

            List<Visit> allSentVisits = visitService
                    .getAllVisitsWithSomeStatusesDoneByDoctor(doctorUser, VisitStatus.SENT);
            allSentVisits = remainOnlyOneClientDataVisitsInList(requiredUser.getClientData(), allSentVisits);
            model.addAttribute("allSentVisits", allSentVisits);

            List<Visit> allActiveVisits = visitService
                    .getAllVisitsWithSomeStatusesDoneByDoctor(doctorUser, VisitStatus.ACTIVE);
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
            ClientData clientUser = authenticatedClientUser.getClientData();

            model.addAttribute("isAuthUserClient", true);

            List<Visit> allVisits = visitService
                    .getAllVisitsByClient(clientUser);
            allVisits = remainOnlyOneDoctorDataVisitsInList(requiredUser.getDoctorData(), allVisits);
            model.addAttribute("allVisits", allVisits);

            List<Visit> allSentAndActiveVisits = visitService
                    .getAllVisitsWithSomeStatusesDoneByClient(clientUser, VisitStatus.SENT, VisitStatus.ACTIVE);
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

        DoctorData doctorUser = principal.getDoctorData();

        List<Visit> allSentVisits = visitService
                .getAllVisitsWithSomeStatusesDoneByDoctor(doctorUser, VisitStatus.SENT);
        model.addAttribute("allSentVisits", allSentVisits);

        Map<Visit, ApplicationUserClient> allSentVisitsByDoctorWithItsClientUsers =
                addClientUsersFromVisitToAllDoctorVisits(allSentVisits);
        model.addAttribute("allSentVisitsMap", allSentVisitsByDoctorWithItsClientUsers);

        List<Visit> allActiveVisits = visitService
                .getAllVisitsWithSomeStatusesDoneByDoctor(doctorUser, VisitStatus.ACTIVE);
        model.addAttribute("allActiveVisits", allActiveVisits);

        List<Visit> allAcceptedVisits = visitService
                .getAllVisitsExceptVisitsWithSomeStatusesDoneByDoctor(doctorUser, VisitStatus.SENT,
                        VisitStatus.CANCELLED_BY_DOCTOR, VisitStatus.CANCELLED_BY_CLIENT);
        model.addAttribute("allAcceptedVisits", allAcceptedVisits);

        Map<Visit, ApplicationUserClient> allAcceptedVisitsByDoctorWithItsClientUsers =
                addClientUsersFromVisitToAllDoctorVisits(allAcceptedVisits);
        model.addAttribute("allAcceptedVisitsMap", allAcceptedVisitsByDoctorWithItsClientUsers);


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

        ClientData clientUser = principal.getClientData();

        List<Visit> allVisits = visitService
                .getAllVisitsByClient(clientUser);
        model.addAttribute("allVisits", allVisits);

        Map<Visit, ApplicationUserDoctor> allVisitsByClientWithItsDoctorUsers =
                addDoctorUsersFromVisitToAllClientVisits(allVisits);
        model.addAttribute("allVisitsMap", allVisitsByClientWithItsDoctorUsers);

        List<Visit> allSentAndActiveVisits = visitService
                .getAllVisitsWithSomeStatusesDoneByClient(clientUser, VisitStatus.SENT, VisitStatus.ACTIVE);
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
