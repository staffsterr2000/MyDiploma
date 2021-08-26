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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Map;

/**
 * Processes all profile initialization business logic.
 * @author staffsterr2000
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class ProfileService {

    /**
     * Visit service
     */
    private final VisitService visitService;

    /**
     * Person data service
     */
    private final PersonDataService personDataService;

    /**
     * Connection with user service
     */
    private final ApplicationUserService applicationUserService;



    /**
     * Initializes model of attributes and returns view depending on
     * whether the authenticated user is a doctor user or a client user.
     * @param authentication current user's authentication.
     * @param model model of attributes.
     * @return view name.
     */
    public String initiateProfile(Authentication authentication, Model model) {
        // add link to redirect back
        model.addAttribute("redirectLink", "profile");

        // get bindingResult
        BindingResult bindingResult = (BindingResult) model.getAttribute("bindingResult");
        boolean error = bindingResult != null;

        // if authenticated user is doctor
        if (authentication.getPrincipal() instanceof ApplicationUserDoctor) {
            // initialize doctor profile
            initiateDoctorProfile(authentication, model);
            if (error)
                // initialize errors if they exist
                addErrorsIntoModel(model, bindingResult);

            return "doctor_profile";
        }

        // if authenticated user is doctor
        if (authentication.getPrincipal() instanceof ApplicationUserClient) {
            // initialize client profile
            initiateClientProfile(authentication, model);
            if (error)
                // initialize errors if they exist
                addErrorsIntoModel(model, bindingResult);

            return "client_profile";
        }

        // else throw exception
        throw new IllegalStateException("Unknown role");

    }



    /**
     * Initializes model of attributes and returns view depending on
     * whether the required user is a doctor user or a client user.
     * @param username username of the user, whose page the current
     *                 user has attended.
     * @param authentication current user's authentication.
     * @param model model of attributes.
     * @return view name.
     */
    public String initiateIdProfile(String username, Authentication authentication, Model model) {
        // add link to redirect back
        model.addAttribute("redirectLink", "id/" + username);

        // get bindingResult
        BindingResult bindingResult = (BindingResult) model.getAttribute("bindingResult");
        boolean error = bindingResult != null;

        // get and add the user, whose profile the authenticated user has attended
        ApplicationUser requiredUser = applicationUserService
                .getByUsername(username);
        model.addAttribute("requiredUser", requiredUser);

        // if required user is a doctor user
        if (requiredUser instanceof ApplicationUserDoctor) {
            // initialize doctor id profile
            initiateDoctorIdProfile((ApplicationUserDoctor) requiredUser, authentication, model);
            if (error)
                // initialize errors if they exist
                addErrorsIntoModel(model, bindingResult);

            return "doctor_id_profile";
        }

        // if required user is a client user
        if (requiredUser instanceof ApplicationUserClient) {
            // initialize client id profile
            initiateClientIdProfile((ApplicationUserClient) requiredUser, authentication, model);
            if (error)
                // initialize errors if they exist
                addErrorsIntoModel(model, bindingResult);

            return "client_id_profile";
        }

        // else throw exception
        throw new IllegalStateException("Unknown role");

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
                visitService.getAllClientVisitsAndDoctorUsersInMap(allVisits);
        // add all SENT and ACTIVE visits
        model.addAttribute("allVisitsMap", allVisitsByClientWithItsDoctorUsers);

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
                visitService.getAllDoctorVisitsAndClientUsersInMap(allSentVisits);
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
                visitService.getAllDoctorVisitsAndClientUsersInMap(allAcceptedVisits);
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
            allVisits = visitService.remainOnlyOneClientDataVisitsInList(
                    requiredUser.getClientData(), allVisits);
            model.addAttribute("allVisits", allVisits);

            // add all SENT to doctor visits
            List<Visit> allSentVisits = visitService
                    .getAllVisitsWithSomeStatusesDoneByDoctor(doctorUser, VisitStatus.SENT);
            // remain only visits with required client user
            allSentVisits = visitService.remainOnlyOneClientDataVisitsInList(
                    requiredUser.getClientData(), allSentVisits);
            model.addAttribute("allSentVisits", allSentVisits);

            // add all ACTIVE visits by doctor
            List<Visit> allActiveVisits = visitService
                    .getAllVisitsWithSomeStatusesDoneByDoctor(doctorUser, VisitStatus.ACTIVE);
            // remain only visits with required client user
            allActiveVisits = visitService.remainOnlyOneClientDataVisitsInList(
                    requiredUser.getClientData(), allActiveVisits);
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
            allVisits = visitService.remainOnlyOneDoctorDataVisitsInList(
                    requiredUser.getDoctorData(), allVisits);
            model.addAttribute("allVisits", allVisits);

            // add all SENT and ACTIVE visits by client
            List<Visit> allSentAndActiveVisits = visitService
                    .getAllVisitsWithSomeStatusesDoneByClient(clientUser, VisitStatus.SENT, VisitStatus.ACTIVE);
            // remain only visits with required doctor user
            allSentAndActiveVisits = visitService.remainOnlyOneDoctorDataVisitsInList(
                    requiredUser.getDoctorData(), allSentAndActiveVisits);
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
