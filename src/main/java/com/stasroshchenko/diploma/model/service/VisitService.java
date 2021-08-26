package com.stasroshchenko.diploma.model.service;

import com.stasroshchenko.diploma.entity.Visit;
import com.stasroshchenko.diploma.entity.person.ClientData;
import com.stasroshchenko.diploma.entity.person.DoctorData;
import com.stasroshchenko.diploma.entity.user.ApplicationUserClient;
import com.stasroshchenko.diploma.entity.user.ApplicationUserDoctor;
import com.stasroshchenko.diploma.model.service.user.ApplicationUserService;
import com.stasroshchenko.diploma.request.visit.*;
import com.stasroshchenko.diploma.model.repository.VisitRepository;
import com.stasroshchenko.diploma.util.VisitStatus;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.stasroshchenko.diploma.util.VisitStatus.*;

/**
 * Processes all visit business logic.
 * @author staffsterr2000
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class VisitService {

    /**
     * Logging
     */
    private final static Logger LOGGER =
            LoggerFactory.getLogger(ProfileService.class);



    /**
     * Visit repo
     */
    private final VisitRepository visitRepository;

    /**
     * Person data service
     */
    private final PersonDataService personDataService;

    /**
     * User service
     */
    private final ApplicationUserService applicationUserService;



    /**
     * Gets list of all visits.
     * @return list of all visits.
     * @since 1.0
     */
    public List<Visit> getAllVisits() {
        return visitRepository.findAllOrdered();
    }



    /**
     * Gets list of visits by certain client.
     * @param clientData client whose visits we're looking for.
     * @return list of visits.
     * @since 1.0
     */
    public List<Visit> getAllVisitsByClient(ClientData clientData) {
        // stream of all visits
        return getAllVisits().stream()
                // seek for client data matching
                .filter(visit -> visit.getClientData().equals(clientData))
                // collect the visits to list
                .collect(Collectors.toList());
    }



    /**
     * Gets list of visits by certain doctor.
     * @param doctorData doctor whose visits we're looking for.
     * @return list of visits.
     * @since 1.0
     */
    public List<Visit> getAllVisitsByDoctor(DoctorData doctorData) {
        // stream of all visits
        return getAllVisits().stream()
                // seek for doctor data matching
                .filter(visit -> visit.getDoctorData().equals(doctorData))
                // collect the visits to list
                .collect(Collectors.toList());
    }



    /**
     * Gets list of visits both by certain client and non excluded
     * visit statuses.
     * @param clientData client whose visits we're looking for.
     * @param statuses excluded statuses.
     * @return list of visits.
     * @since 1.0
     */
    public List<Visit> getAllVisitsExceptVisitsWithSomeStatusesDoneByClient(
            ClientData clientData, VisitStatus... statuses) {
        // stream of all visits by client
        return getAllVisitsByClient(clientData).stream()
                // seek through the visits' statuses for non matching with statuses
                // from method parameter 'statuses'
                .filter(visit -> Arrays.stream(statuses)
                        .map(status -> !visit.getStatus().equals(status))
                        // if the current visit from stream matches at least
                        // one status from 'statuses' .filter will be false
                        // and the visit won't be added to list.
                        .reduce(true, (acc, x) -> acc && x)
                )
                // collect all the visits
                .collect(Collectors.toList());
    }



    /**
     * Gets list of visits by both certain doctor and non excluded
     * visit statuses.
     * @param doctorData doctor whose visits we're looking for.
     * @param statuses excluded statuses.
     * @return list of visits.
     * @since 1.0
     */
    public List<Visit> getAllVisitsExceptVisitsWithSomeStatusesDoneByDoctor(
            DoctorData doctorData, VisitStatus... statuses) {
        // stream of all visits by doctor
        return getAllVisitsByDoctor(doctorData).stream()
                // seek through the visits' statuses for non matching with statuses
                // from method parameter 'statuses'
                .filter(visit -> Arrays.stream(statuses)
                        .map(status -> !visit.getStatus().equals(status))
                        // if the current visit from stream matches at least
                        // one status from 'statuses' .filter will be false
                        // and the visit won't be added to list.
                        .reduce(true, (acc, x) -> acc && x)
                )
                // collect all the visits
                .collect(Collectors.toList());
    }



    /**
     * Gets list of visits by both certain client and included visit
     * statuses.
     * @param clientData client whose visits we're looking for.
     * @param statuses included statuses.
     * @return list of visits.
     * @since 1.0
     */
    public List<Visit> getAllVisitsWithSomeStatusesDoneByClient(
            ClientData clientData, VisitStatus... statuses) {
        // stream of all 'statuses'
        return Arrays.stream(statuses)
                // the second stream - visits by client
                .flatMap(status -> getAllVisitsByClient(clientData).stream()
                        // if the visit status matches a status from 'statuses'
                        .filter(visit -> visit.getStatus().equals(status)))
                // then collect the visit to list
                .collect(Collectors.toList());
    }



    /**
     * Gets list of visits by both certain doctor and included visit
     * statuses.
     * @param doctorData doctor whose visits we're looking for.
     * @param statuses included statuses.
     * @return list of visits.
     * @since 1.0
     */
    public List<Visit> getAllVisitsWithSomeStatusesDoneByDoctor(
            DoctorData doctorData, VisitStatus... statuses) {
        // stream of all 'statuses'
        return Arrays.stream(statuses)
                // the second stream - visits by doctor
                .flatMap(status -> getAllVisitsByDoctor(doctorData).stream()
                        // if the visit status matches a status from 'statuses'
                        .filter(visit -> visit.getStatus().equals(status)))
                // then collect the visit to list
                .collect(Collectors.toList());
    }



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
    public Map<Visit, ApplicationUserClient> getAllDoctorVisitsAndClientUsersInMap(
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
    public Map<Visit, ApplicationUserDoctor> getAllClientVisitsAndDoctorUsersInMap(
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
     * Gets visit by its id.
     * @param id id of the visit we're looking for.
     * @return required visit.
     * @throws IllegalStateException if visit with such id isn't found.
     * @since 1.0
     */
    public Visit getVisitById(Long id) {
        return visitRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(String.format("Visit with id %d wasn't found", id)));
    }



    /**
     * Saves visit to DB.
     * @param visit visit to save.
     * @since 1.0
     */
    public void saveVisit(Visit visit) {
        visitRepository.save(visit);
    }



    /**
     * Checks for either SENT or ACTIVE visit presence between
     * the client and the doctor, and then 'sends' the visit.
     * @param clientUser client, who 'sends' the visit.
     * @param request send request.
     * @since 1.0
     * @see Visit
     */
    public void sendVisit(ClientData clientUser, SendVisitRequest request) {
        Long doctorDataId = request.getDoctorDataId();
        String complaint = request.getComplaint();

        // take doctor by id
        DoctorData doctorData = personDataService
                .getDoctorById(doctorDataId);

        // check is the client and the doctor have neither
        // SENT nor ACTIVE visits
        boolean isClientAndDoctorHaveNeitherSentNorActiveVisit =
                isClientAndDoctorHaveNeitherSentNorActiveVisit(clientUser, doctorData);

        // if they don't have one
        if (isClientAndDoctorHaveNeitherSentNorActiveVisit) {
            // create new SENT visit
            Visit visitToSend = new Visit(
                    doctorData,
                    clientUser,
                    complaint,
                    null,
                    null,
                    null,
                    SENT
            );

            // save it
            visitRepository.save(visitToSend);

        } else {
            // else throw exception
            throw new IllegalStateException("You have already sent a request to this doctor.");
        }

    }



    /**
     * Checks for either SENT or ACTIVE visit presence between
     * the doctor and the client, then checks for correctness of
     * appointment time and then 'creates' the visit.
     * @param doctorUser doctor, who 'creates' the visit.
     * @param request create request.
     * @since 1.0
     * @see Visit
     */
    public void createVisit(DoctorData doctorUser, CreateVisitRequest request) {
        LocalDateTime visitDate = request.getAppointsAt();

        // create new client
        ClientData newClient = new ClientData(
                request.getClientFirstName(),
                request.getClientLastName(),
                request.getClientDateOfBirth(),
                request.getClientPassportId()
        );

        // check is the client and the doctor have neither
        // SENT nor ACTIVE visits
        boolean isClientAndDoctorHaveNeitherSentNorActiveVisit =
                isClientAndDoctorHaveNeitherSentNorActiveVisit(newClient, doctorUser);

        // if they don't have one
        if (isClientAndDoctorHaveNeitherSentNorActiveVisit) {
            // check for correctness of appointment time
            isDateValidForDoctor(doctorUser, visitDate);

            // create new client or return existing
            ClientData clientDataFromDB = personDataService
                    .signUpClientData(newClient);

            // create new visit
            Visit visit = new Visit(
                    doctorUser,
                    clientDataFromDB,
                    request.getClientComplaint(),
                    LocalDateTime.now(),
                    visitDate,
                    request.getAppointsAtInput(),
                    ACTIVE
            );

            // save the visit
            visitRepository.save(visit);

        } else {
            // else throw exception
            throw new IllegalStateException("You have already has a visit with this client.");
        }

    }



    /**
     * Checks for the appointment time validness and 'accepts'
     * the visit.
     * @param doctorUser doctor, who 'accepts' the visit.
     * @param request accept request.
     * @since 1.0
     * @see Visit
     */
    public void acceptVisit(DoctorData doctorUser, AcceptVisitRequest request) {
        Long visitId = request.getVisitId();
        LocalDateTime visitDate = request.getAppointsAt();

        // check for appointment time validness
        isDateValidForDoctor(doctorUser, visitDate);

        // seek the visit from DB by the visit id
        Visit visitFromDatabase = getVisitById(visitId);

        // if visit from DB has status SENT
        if (visitFromDatabase.getStatus().equals(SENT) &&
                // and the given doctor is the visit's doctor
                doctorUser.equals(visitFromDatabase.getDoctorData())) {

            // set all the data
            visitFromDatabase.setAppointsAt(visitDate);
            visitFromDatabase.setAcceptedAt(LocalDateTime.now());
            visitFromDatabase.setStatus(ACTIVE);

            // save the visit
            visitRepository.save(visitFromDatabase);
        }

    }



    /**
     * 'Declines' the visit.
     * @param doctorUser doctor, who 'declines' the visit.
     * @param request decline request.
     * @since 1.0
     * @see Visit
     */
    public void declineVisit(DoctorData doctorUser, DeclineVisitRequest request) {
        Long visitId = request.getVisitId();

        // seek the visit from DB by the visit id
        Visit visit = getVisitById(visitId);

        // if visit from DB has status SENT
        if (visit.getStatus().equals(SENT) &&
                // and the given doctor is the visit's doctor
                doctorUser.equals(visit.getDoctorData())) {

            // set all the data
            visit.setStatus(CANCELLED_BY_DOCTOR);

            // save the visit
            visitRepository.save(visit);
        }

    }



    /**
     * 'Cancels' the visit.
     * @param clientUser client, who 'cancels' the visit.
     * @param request cancel request.
     * @since 1.0
     * @see Visit
     */
    public void cancelVisit(ClientData clientUser, CancelVisitRequest request) {
        Long visitId = request.getVisitId();

        // seek the visit from DB by the visit id
        Visit visit = getVisitById(visitId);

        // if visit from DB has status SENT or ACTIVE
        if ((visit.getStatus().equals(ACTIVE) ||
                (visit.getStatus().equals(SENT))) &&
                // and the given client is the visit's client
                clientUser.equals(visit.getClientData())) {

            // set all the data
            visit.setStatus(CANCELLED_BY_CLIENT);

            // save the visit
            visitRepository.save(visit);
        }

    }



    /**
     * Checks income visit status's correctness
     * (OCCURRED/NOT_OCCURRED/CANCELLED_BY_DOCTOR) and then
     * 'passes' the visit.
     * @param doctorUser doctor, who 'passes' the visit.
     * @param request pass request.
     * @since 1.0
     * @see Visit
     * @see VisitStatus
     */
    public void passVisit(DoctorData doctorUser, PassVisitRequest request) {
        Long visitId = request.getVisitId();
        VisitStatus visitStatus = request.getStatus();

        // seek the visit from DB by the visit id
        Visit visit = getVisitById(visitId);

        // the given visit status must be one of these statuses
        switch (visitStatus) {
            case OCCURRED:
            case NOT_OCCURRED:
            case CANCELLED_BY_DOCTOR:
                break;
            default:
                // else throw exception
                throw new IllegalStateException("Status " + visitStatus + " is wrong.");
        }

        // if visit from DB has status ACTIVE
        if (visit.getStatus().equals(ACTIVE) &&
                // and the given doctor is the visit's doctor
                doctorUser.equals(visit.getDoctorData())) {

            // if the appointment time has already gone
            if (visit.getAppointsAt().isBefore(LocalDateTime.now()) ||
                    // or the given status is CANCELLED_BY_DOCTOR
                    visitStatus.equals(CANCELLED_BY_DOCTOR)) {

                // set all the data
                visit.setStatus(visitStatus);

                // save the visit
                visitRepository.save(visit);

            } else {
                // else throw exception
                throw new IllegalStateException("The time hasn't come");
            }
        }

    }



    /**
     * Validates that appointment time is free for doctor.
     * Two dates must not collide for 1 hour relative to
     * the appointment time.
     * For example if one appointment is at 8 o'clock -
     * next must be at least at 9 o'clock.
     * @param doctorUser doctor, who created/accepted a visit.
     * @param visitDate appointment time for validation.
     * @return bool, about business of the appointment time
     * (true - time is free for this doctor).
     * @since 1.0
     */
    private boolean isTimeFreeForDoctor(DoctorData doctorUser, LocalDateTime visitDate) {
        if (visitDate == null) return false;

        // stream of all visits by doctor
        return getAllVisitsByDoctor(doctorUser).stream()
                // seek for ACTIVE visits
                .filter(visit -> visit.getStatus().equals(VisitStatus.ACTIVE))
                // seek for visit with same appointment time
                .filter(visit -> {
                    // visit from stream's appointment time
                    LocalDateTime appointsAt = visit.getAppointsAt();
                    return
                            // is given date after or equals appointment time
                            ((visitDate.isAfter(appointsAt) || visitDate.isEqual(appointsAt)) &&
                                    // and given date is before (appointment time + 1 hour)
                                    (visitDate.isBefore(appointsAt.plusHours(1)))) ||
                                    // OR (given date + 1 hour) is after appointment time
                                    ((visitDate.plusHours(1).isAfter(appointsAt)) &&
                                            // and (given date + 1 hour) is before or equals (appointment time + 1 hour)
                                            (visitDate.plusHours(1).isBefore(appointsAt.plusHours(1)) ||
                                                    visitDate.plusHours(1).isEqual(appointsAt.plusHours(1))));
                })
                // find any visit with such collision
                .findAny()
                // if empty - this time is free for the doctor
                .isEmpty();
    }



    /**
     * Validates that appointment time is both within workday and
     * at least tomorrow.
     * @param visitDate appointment time for validation.
     * @return bool, about correctness of the appointment
     * time (true - time is valid).
     * @since 1.0
     */
    private boolean isDateWithinWorkdayAndAtLeastTomorrow(LocalDateTime visitDate) {
        if (visitDate == null) return false;

        // time when workday starts
        LocalTime timeWorkdayStarts = LocalTime.of(8, 0);

        // time when workday ends - 1 hour
        LocalTime timeWorkdayEnds = LocalTime.of(18, 0);
        LocalTime timeWorkdayEndsMinusOneHour = timeWorkdayEnds.minusHours(1);

        // tomorrow
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        return
                // is given date after or equals (tomorrow 8 o'clock)
                (visitDate.isAfter(LocalDateTime.of(tomorrow, timeWorkdayStarts)) ||
                        visitDate.isEqual(LocalDateTime.of(tomorrow, timeWorkdayStarts))) &&
                        // is given date before of equals (tomorrow 18 o'clock - 1 hour)
                        (visitDate.toLocalTime().isBefore(timeWorkdayEndsMinusOneHour) ||
                                visitDate.toLocalTime().equals(timeWorkdayEndsMinusOneHour));

    }



    /**
     * Validates appointment time by both validation methods
     * and throws exception if appointment time is invalid.
     * @param doctorUser doctor, who created/accepted a visit.
     * @param visitDate appointment time for validation.
     * @throws IllegalStateException if appointment time for
     * some reason is invalid.
     * @since 1.0
     */
    private void isDateValidForDoctor(DoctorData doctorUser, LocalDateTime visitDate) {
        if (!isDateWithinWorkdayAndAtLeastTomorrow(visitDate)) {
            throw new IllegalStateException("Date is invalid. Try using \"0\" before the next digit or time within 8 and 18 o'clock from tomorrow. Example (08:09 11/01/2000)");
        }
        if (!isTimeFreeForDoctor(doctorUser, visitDate)) {
            throw new IllegalStateException("This time has already been taken for another visit. Try another time");
        }
    }



    /**
     * Checks that between client and doctor isn't present
     * a visit with SENT or ACTIVE visit status.
     * Used to permit creation of only one SENT or ACTIVE
     * visit between client user and doctor user.
     * @param clientData doctor with the same visit
     * @param doctorData client with the same visit
     * @return bool about absence of both SENT and
     * ACTIVE visits between the client and the doctor
     * (true - those visits are absent).
     * @since 1.0
     * @see VisitStatus
     * @see Visit
     */
    private boolean isClientAndDoctorHaveNeitherSentNorActiveVisit(
            ClientData clientData, DoctorData doctorData) {
        // stream of all visits by client
        return getAllVisitsByClient(clientData).stream()
                // seek for a visit with the given doctor
                .filter(visit -> visit.getDoctorData().equals(doctorData))
                // seek for a visit with status SENT or ACTIVE
                .filter(visit -> {
                    VisitStatus status = visit.getStatus();
                    return status.equals(SENT) || status.equals(ACTIVE);
                })
                // find first
                .findFirst()
                // if empty - there is full absence of those statuses
                // or visits between the client and the doctor at all
                .isEmpty();
    }

}
