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

/**
 * Processes all profile business logic.
 * @author staffsterr2000
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class ProfileService {

    /**
     * Logging
     */
    private final static Logger LOGGER =
            LoggerFactory.getLogger(ProfileService.class);



    /**
     * Visit service
     */
    private final VisitService visitService;

    /**
     * Person data service
     */
    private final PersonDataService personDataService;

    /**
     * User service
     */
    private final ApplicationUserService applicationUserService;



    /**
     * Lefts only one client's visits in the source list.
     * @param clientData client, whose visits is needed to
     *                   remain in the source list.
     * @param source source list with visits.
     * @return list of visits for certain client.
     * @since 1.0
     */
    public List<Visit> remainOnlyOneClientDataVisitsInList(
            ClientData clientData, List<Visit> source) {

        // from source visit list
        return source.stream()
                // seek visits where client data is the certain client
                .filter(visit -> visit.getClientData()
                        .equals(clientData))
                // collect them into list and return it
                .collect(Collectors.toList());
    }



    /**
     * Lefts only one doctor's visits in the source list.
     * @param doctorData doctor, whose visits is needed to
     *                   remain in the source list.
     * @param source source list with visits.
     * @return list of visits for certain doctor.
     * @since 1.0
     */
    public List<Visit> remainOnlyOneDoctorDataVisitsInList(
            DoctorData doctorData, List<Visit> source) {

        // form source visit list
        return source.stream()
                // seek visits where doctor data is the certain doctor
                .filter(visit -> visit.getDoctorData()
                        .equals(doctorData))
                // collect them into the list and return it
                .collect(Collectors.toList());
    }



    /**
     * Runs through source list, links visit to its client user
     * and writes it to map (key - visit, value - client user).
     * At the end of the day we have map of visits as keys and
     * either client users or nulls as values.
     * @param source source list with doctor's visits.
     * @return map of visits linked to its client users.
     * @since 1.0
     */
    public Map<Visit, ApplicationUserClient> getDoctorVisitsAndClientUsersInMap(
            List<Visit> source) {
        Map<Visit, ApplicationUserClient>
                allVisitsByDoctorWithItsClientUsers = new LinkedHashMap<>();

        // run through all source visits
        for (Visit visit : source) {
            ClientData clientData = visit.getClientData();
            ApplicationUserClient applicationUserClient;
            try {
                // try to find client user by client
                applicationUserClient = applicationUserService.getByClientData(clientData);

            } catch (Exception ex) {
                // catch, log
                LOGGER.error(ex.getMessage());
                applicationUserClient = null;
            }

            // put to map (user client == null if client has no account)
            allVisitsByDoctorWithItsClientUsers.put(visit, applicationUserClient);
        }

        // return map
        return allVisitsByDoctorWithItsClientUsers;
    }



    /**
     * Runs through source list, links visit to its doctor user
     * and puts it down to map (key - visit, value - doctor user).
     * At the end of the day we have map of visits as keys and
     * either doctor users or nulls as values.
     * @param source source list with client's visits.
     * @return map of visits linked to its doctor users.
     * @since 1.0
     */
    public Map<Visit, ApplicationUserDoctor> getClientVisitsAndDoctorUsersInMap(
            List<Visit> source) {
        Map<Visit, ApplicationUserDoctor>
                allVisitsByClientWithItsDoctorUsers = new LinkedHashMap<>();

        // run through all source visits
        for (Visit visit : source) {
            DoctorData doctorData = visit.getDoctorData();
            ApplicationUserDoctor applicationUserDoctor;
            try {
                // try to find doctor user by doctor
                applicationUserDoctor = applicationUserService.getByDoctorData(doctorData);

            } catch (Exception ex) {
                // catch, log
                LOGGER.error(ex.getMessage());
                applicationUserDoctor = null;
            }

            // put to map (user doctor == null if doctor has no account)
            allVisitsByClientWithItsDoctorUsers.put(visit, applicationUserDoctor);
        }

        // return map
        return allVisitsByClientWithItsDoctorUsers;
    }



    /**
     * Initializes model of attributes with errors from bindingResult for
     * displaying them in the profile.
     * @param model model of attributes.
     * @param result result of validation and binding (contains error objects).
     * @since 1.0
     */
    public void addErrorsIntoModel(
            Model model, BindingResult result) {

        // for every global error
        for (ObjectError objectError : result.getGlobalErrors()) {
            // add the error to the model
            model.addAttribute(objectError.getObjectName(), objectError);
        }

        // for every field error
        for (FieldError fieldError : result.getFieldErrors()) {
            // add the error to the model
            model.addAttribute(fieldError.getObjectName() + "_" + fieldError.getField() + "Error", fieldError);
        }

    }



    /**
     * Initializes model of attributes with necessary data for displaying
     * own client's profile.
     * @param authentication current user's authentication
     * @param model model of attributes
     * @since 1.0
     */
    public void initiateClientProfile(
            Authentication authentication, Model model) {

        // add authentication
        ApplicationUserClient principal =
                (ApplicationUserClient) authentication.getPrincipal();
        model.addAttribute("principal", principal);

        ClientData clientUser = principal.getClientData();

        // add all visits by client
        List<Visit> allVisits = visitService
                .getAllVisitsByClient(clientUser);
        model.addAttribute("allVisits", allVisits);

        // add map of client's visits, to their doctor users
        Map<Visit, ApplicationUserDoctor> allVisitsByClientWithItsDoctorUsers =
                getClientVisitsAndDoctorUsersInMap(allVisits);
        model.addAttribute("allVisitsMap", allVisitsByClientWithItsDoctorUsers);

        // add all SENT and ACTIVE visits
        List<Visit> allSentAndActiveVisits = visitService
                .getAllVisitsWithSomeStatusesDoneByClient(clientUser, VisitStatus.SENT, VisitStatus.ACTIVE);
        model.addAttribute("allSentAndActiveVisits", allSentAndActiveVisits);

        // add all doctors
        List<DoctorData> allDoctors =
                personDataService.getAllDoctors();
        model.addAttribute("allDoctors", allDoctors);

        // add empty send request if not exist
        if (model.getAttribute("sendRequest") == null) {
            model.addAttribute("sendRequest", new SendVisitRequest());
        }
        // add empty cancel request if not exist
        if (model.getAttribute("cancelRequest") == null) {
            model.addAttribute("cancelRequest", new CancelVisitRequest());
        }

    }



    /**
     * Initializes model of attributes with necessary data for displaying
     * own doctor's profile.
     * @param authentication current user's authentication
     * @param model model of attributes
     * @since 1.0
     */
    public void initiateDoctorProfile(
            Authentication authentication, Model model) {

        // add authentication
        ApplicationUserDoctor principal =
                (ApplicationUserDoctor) authentication.getPrincipal();
        model.addAttribute("principal", principal);

        DoctorData doctorUser = principal.getDoctorData();

        // add all SENT to doctor visits
        List<Visit> allSentVisits = visitService
                .getAllVisitsWithSomeStatusesDoneByDoctor(doctorUser, VisitStatus.SENT);
        model.addAttribute("allSentVisits", allSentVisits);

        // add map of SENT to doctor visits, to their client users
        Map<Visit, ApplicationUserClient> allSentVisitsByDoctorWithItsClientUsers =
                getDoctorVisitsAndClientUsersInMap(allSentVisits);
        model.addAttribute("allSentVisitsMap", allSentVisitsByDoctorWithItsClientUsers);

        // add all ACTIVE visits by doctor
        List<Visit> allActiveVisits = visitService
                .getAllVisitsWithSomeStatusesDoneByDoctor(doctorUser, VisitStatus.ACTIVE);
        model.addAttribute("allActiveVisits", allActiveVisits);

        // add all visits by doctor except visits with SENT and CANCELLED statuses
        List<Visit> allAcceptedVisits = visitService
                .getAllVisitsExceptVisitsWithSomeStatusesDoneByDoctor(doctorUser, VisitStatus.SENT,
                        VisitStatus.CANCELLED_BY_DOCTOR, VisitStatus.CANCELLED_BY_CLIENT);
        model.addAttribute("allAcceptedVisits", allAcceptedVisits);

        // add map of all visits by doctor except visits SENT and CANCELLED statuses,
        // to their client users
        Map<Visit, ApplicationUserClient> allAcceptedVisitsByDoctorWithItsClientUsers =
                getDoctorVisitsAndClientUsersInMap(allAcceptedVisits);
        model.addAttribute("allAcceptedVisitsMap", allAcceptedVisitsByDoctorWithItsClientUsers);


        // add empty accept request if not exist
        if (model.getAttribute("acceptRequest") == null) {
            model.addAttribute("acceptRequest", new AcceptVisitRequest());
        }
        // add empty decline request if not exist
        if (model.getAttribute("declineRequest") == null) {
            model.addAttribute("declineRequest", new DeclineVisitRequest());
        }
        // add empty pass request if not exist
        if (model.getAttribute("passRequest") == null) {
            model.addAttribute("passRequest", new PassVisitRequest());
        }
        // add empty create request if not exist
        if (model.getAttribute("createRequest") == null) {
            model.addAttribute("createRequest", new CreateVisitRequest());
        }

    }



    /**
     * Initializes model of attributes with necessary data for displaying
     * client's profile, as profile of another user (ID profile).
     * @param requiredUser client user whose page the current user has attended
     * @param authentication current user's authentication
     * @param model model of attributes
     * @since 1.0
     */
    public void initiateClientIdProfile(
            ApplicationUserClient requiredUser,
            Authentication authentication,
            Model model) {

        // current user
        ApplicationUser authenticatedUser =
                (ApplicationUser) authentication.getPrincipal();

        // if current user is a doctor user
        if (authenticatedUser instanceof ApplicationUserDoctor) {
            ApplicationUserDoctor authenticatedDoctorUser =
                    (ApplicationUserDoctor) authenticatedUser;
            DoctorData doctorUser = authenticatedDoctorUser.getDoctorData();

            // add flag that current user is a doctor user
            model.addAttribute("isAuthUserDoctor", true);

            // add all visits by doctor
            List<Visit> allVisits = visitService
                    .getAllVisitsByDoctor(doctorUser);
            // remain only visits with required client user
            allVisits = remainOnlyOneClientDataVisitsInList(
                    requiredUser.getClientData(), allVisits);
            model.addAttribute("allVisits", allVisits);

            // add all SENT to doctor visits
            List<Visit> allSentVisits = visitService
                    .getAllVisitsWithSomeStatusesDoneByDoctor(doctorUser, VisitStatus.SENT);
            // remain only visits with required client user
            allSentVisits = remainOnlyOneClientDataVisitsInList(requiredUser.getClientData(), allSentVisits);
            model.addAttribute("allSentVisits", allSentVisits);

            // add all ACTIVE visits by doctor
            List<Visit> allActiveVisits = visitService
                    .getAllVisitsWithSomeStatusesDoneByDoctor(doctorUser, VisitStatus.ACTIVE);
            // remain only visits with required client user
            allActiveVisits = remainOnlyOneClientDataVisitsInList(requiredUser.getClientData(), allActiveVisits);
            model.addAttribute("allActiveVisits", allActiveVisits);

            // add empty accept request in not exist
            if (model.getAttribute("acceptRequest") == null) {
                model.addAttribute("acceptRequest", new AcceptVisitRequest());
            }
            // add empty accept request in not exist
            if (model.getAttribute("declineRequest") == null) {
                model.addAttribute("declineRequest", new DeclineVisitRequest());
            }
            // add empty accept request in not exist
            if (model.getAttribute("passRequest") == null) {
                model.addAttribute("passRequest", new PassVisitRequest());
            }

        }
    }



    /**
     * Initializes model of attributes with necessary data for displaying
     * doctor's profile, as profile of another user (ID profile).
     * @param requiredUser doctor user whose page the current user has attended
     * @param authentication current user's authentication
     * @param model model of attributes
     * @since 1.0
     */
    public void initiateDoctorIdProfile(
            ApplicationUserDoctor requiredUser,
            Authentication authentication,
            Model model) {

        // current user
        ApplicationUser authenticatedUser =
                (ApplicationUser) authentication.getPrincipal();

        // if current user is a client user
        if (authenticatedUser instanceof ApplicationUserClient) {
            ApplicationUserClient authenticatedClientUser =
                    (ApplicationUserClient) authenticatedUser;
            ClientData clientUser = authenticatedClientUser.getClientData();

            // add flag that current user is a client user
            model.addAttribute("isAuthUserClient", true);

            // add all visits by client
            List<Visit> allVisits = visitService
                    .getAllVisitsByClient(clientUser);
            // remain only visits with required doctor user
            allVisits = remainOnlyOneDoctorDataVisitsInList(requiredUser.getDoctorData(), allVisits);
            model.addAttribute("allVisits", allVisits);

            // add all SENT and ACTIVE visits by client
            List<Visit> allSentAndActiveVisits = visitService
                    .getAllVisitsWithSomeStatusesDoneByClient(clientUser, VisitStatus.SENT, VisitStatus.ACTIVE);
            // remain only visits with required doctor user
            allSentAndActiveVisits = remainOnlyOneDoctorDataVisitsInList(requiredUser.getDoctorData(), allSentAndActiveVisits);
            model.addAttribute("allSentAndActiveVisits", allSentAndActiveVisits);

            // add empty send request if not exist
            if (model.getAttribute("sendRequest") == null) {
                model.addAttribute("sendRequest", new SendVisitRequest());
            }
            // add empty cancel request if not exist
            if (model.getAttribute("cancelRequest") == null) {
                model.addAttribute("cancelRequest", new CancelVisitRequest());
            }
        }

    }

}
