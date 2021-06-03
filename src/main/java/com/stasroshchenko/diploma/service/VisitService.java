package com.stasroshchenko.diploma.service;

import com.stasroshchenko.diploma.entity.request.visit.*;
import com.stasroshchenko.diploma.entity.database.Visit;
import com.stasroshchenko.diploma.entity.database.person.ClientData;
import com.stasroshchenko.diploma.entity.database.person.DoctorData;
import com.stasroshchenko.diploma.repository.VisitRepository;
import com.stasroshchenko.diploma.util.VisitStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.stasroshchenko.diploma.util.VisitStatus.*;

@Service
@AllArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;
    private final PersonDataService personDataService;

    public List<Visit> getAllVisits() {
        return visitRepository.findAll();
    }

    public List<Visit> getAllVisitsOrdered() {
        return visitRepository.findAllOrdered();
    }

    public List<Visit> getAllVisitsByClientOrdered(ClientData clientData) {
        return getAllVisitsOrdered().stream()
                .filter(visit -> visit.getClientData().equals(clientData))
                .collect(Collectors.toList());
    }

    public List<Visit> getAllVisitsByDoctorOrdered(DoctorData doctorData) {
        return getAllVisitsOrdered().stream()
                .filter(visit -> visit.getDoctorData().equals(doctorData))
                .collect(Collectors.toList());
    }

    public List<Visit> getAllVisitsByClient(ClientData clientData) {
        return getAllVisits().stream()
                .filter(visit -> visit.getClientData().equals(clientData))
                .collect(Collectors.toList());
    }

    public List<Visit> getAllVisitsByDoctor(DoctorData doctorData) {
        return getAllVisits().stream()
                .filter(visit -> visit.getDoctorData().equals(doctorData))
                .collect(Collectors.toList());
    }

    public List<Visit> getAllVisitsExceptVisitsWithSomeStatusesOrdered(VisitStatus... statuses) {
        return getAllVisitsOrdered().stream()
                .filter(visit -> Arrays.stream(statuses)
                        .map(status -> !visit.getStatus().equals(status))
                        .reduce(true, (acc, x) -> acc && x)
                )
                .collect(Collectors.toList());

//        List<Visit> visits = new ArrayList<>();
//        for (Visit visitFromDB : getAllVisitsOrdered()) {
//            boolean answer = true;
//            for (VisitStatus status : statuses) {
//                answer &= !visitFromDB.getStatus().equals(status);
//            }
//
//            if (answer) visits.add(visitFromDB);
//        }
//        return visits;
    }

    // check for bugs
    public List<Visit> getAllVisitsWithSomeStatusesOrdered(VisitStatus... statuses) {
        return Arrays.stream(statuses)
                .flatMap(status -> getAllVisitsOrdered().stream()
                        .filter(visit -> visit.getStatus().equals(status)))
                .collect(Collectors.toList());
    }

    public Visit getVisitById(Long id) {
        return visitRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(String.format("Visit with id %d wasn't found", id)));
    }



    public void saveVisit(Visit visit) {

        visitRepository.save(visit);
    }

    private boolean isTimeFree(LocalDateTime visitDate) {
        if (visitDate == null) return false;

        return getAllVisitsOrdered().stream()
                .filter(visit -> visit.getStatus().equals(VisitStatus.ACTIVE))
                .filter(visit -> {
                    LocalDateTime appointsAt = visit.getAppointsAt();
                    return ((visitDate.isAfter(appointsAt) || visitDate.isEqual(appointsAt)) &&
                            (visitDate.isBefore(appointsAt.plusHours(1)))) ||
                            ((visitDate.plusHours(1).isAfter(appointsAt)) &&
                                    (visitDate.plusHours(1).isBefore(appointsAt.plusHours(1)) ||
                                            visitDate.plusHours(1).isEqual(appointsAt.plusHours(1))));
                })
                .findAny()
                .isEmpty();
    }

    private boolean isDateIsWithinWorkdayAndAtLeastTomorrow(LocalDateTime visitDate) {
        if (visitDate == null) return false;

        LocalTime timeWorkdayStarts = LocalTime.of(8, 0);
        LocalTime timeWorkdayEnds = LocalTime.of(18, 0);
        LocalDate today = LocalDate.now();

        return visitDate.isAfter(LocalDateTime.of(today, timeWorkdayStarts)) &&
                visitDate.toLocalTime().isAfter(timeWorkdayStarts) &&
                visitDate.toLocalTime().isBefore(timeWorkdayEnds);
    }

    public void isDateValid(LocalDateTime visitDate) {
        if (!isDateIsWithinWorkdayAndAtLeastTomorrow(visitDate)) {
            throw new IllegalStateException("Date is invalid. Try using \"0\" before another number. Example (08:09 11/01/2000)");
        }
        if (!isTimeFree(visitDate)) {
            throw new IllegalStateException("This time has already been taken for another visit. Try another time");
        }
    }

    public boolean isClientAndDoctorHaveNeitherSentNorActiveVisit(ClientData clientData, DoctorData doctorData) {
        return getAllVisitsByClient(clientData).stream()
                .filter(visit -> visit.getDoctorData().equals(doctorData))
                .filter(visit -> {
                    VisitStatus status = visit.getStatus();
                    return status.equals(SENT) || status.equals(ACTIVE);
                })
                .findFirst()
                .isEmpty();
    }

    public void sendVisit(ClientData clientUser, SendVisitRequest request) {
        Long doctorDataId = request.getDoctorDataId();
        String complaint = request.getComplaint();

        DoctorData doctorData = personDataService
                .getDoctorById(doctorDataId);

        boolean isClientAndDoctorHaveNeitherSentNorActiveVisit =
                isClientAndDoctorHaveNeitherSentNorActiveVisit(clientUser, doctorData);

        if (isClientAndDoctorHaveNeitherSentNorActiveVisit) {
//        if (true) {
            Visit visitToSend = new Visit(
                    doctorData,
                    clientUser,
                    complaint,
                    null,
                    null,
                    null,
                    SENT
            );

            visitRepository.save(visitToSend);

        } else {
            throw new IllegalStateException("You have already sent a request to this doctor.");
        }

    }

    // 1. check for visits -> throw exception
    // 2. check for date -> throw exception
    // 3. register or just take client from db
    // 4. create visit
    public void createVisit(DoctorData doctorUser, CreateVisitRequest request) {
        LocalDateTime visitDate = request.getAppointsAt();

        ClientData newClient = new ClientData(
                request.getClientFirstName(),
                request.getClientLastName(),
                request.getClientDateOfBirth()
        );

        boolean isClientAndDoctorHaveNeitherSentNorActiveVisit =
                isClientAndDoctorHaveNeitherSentNorActiveVisit(newClient, doctorUser);

        if (isClientAndDoctorHaveNeitherSentNorActiveVisit) {
            isDateValid(visitDate);

            ClientData clientDataFromDB = personDataService
                    .signUpClientData(newClient);

            Visit visit = new Visit(
                    doctorUser,
                    clientDataFromDB,
                    request.getClientComplaint(),
                    LocalDateTime.now(),
                    visitDate,
                    request.getClientDateOfBirthInput(),
                    ACTIVE
            );

            visitRepository.save(visit);

        } else {
            throw new IllegalStateException("You have already has a visit with this client.");
        }

    }

    public void acceptVisit(DoctorData doctorUser, AcceptVisitRequest request) {
        Long visitId = request.getVisitId();
        LocalDateTime visitDate = request.getAppointsAt();

        isDateValid(visitDate);

        Visit visitFromDatabase = getVisitById(visitId);

        if (visitFromDatabase.getStatus().equals(SENT) &&
                doctorUser.equals(visitFromDatabase.getDoctorData())) {

            visitFromDatabase.setAppointsAt(visitDate);
            visitFromDatabase.setAcceptedAt(LocalDateTime.now());
            visitFromDatabase.setStatus(ACTIVE);

            visitRepository.save(visitFromDatabase);
        }

    }

    public void declineVisit(DoctorData doctorUser, DeclineVisitRequest request) {
        Long visitId = request.getVisitId();

        Visit visit = getVisitById(visitId);

        if (visit.getStatus().equals(SENT) &&
                doctorUser.equals(visit.getDoctorData())) {

            visit.setStatus(CANCELLED_BY_DOCTOR);
            visitRepository.save(visit);
        }

    }

    public void cancelVisit(ClientData clientUser, CancelVisitRequest request) {
        Long visitId = request.getVisitId();

        Visit visit = getVisitById(visitId);

        if ((visit.getStatus().equals(ACTIVE) ||
                (visit.getStatus().equals(SENT))) &&
                clientUser.equals(visit.getClientData())) {

            visit.setStatus(CANCELLED_BY_CLIENT);
            visitRepository.save(visit);
        }
    }

    public void passVisit(DoctorData doctorUser, PassVisitRequest request) {
        Long visitId = request.getVisitId();
        VisitStatus visitStatus = request.getStatus();

        Visit visit = getVisitById(visitId);

        switch (visitStatus) {
            case OCCURRED:
            case NOT_OCCURRED:
            case CANCELLED_BY_DOCTOR:
                break;
            default:
                throw new IllegalStateException("Status " + visitStatus + " is wrong.");
        }

        if (visit.getStatus().equals(ACTIVE) &&
                doctorUser.equals(visit.getDoctorData())) {

            if (visit.getAppointsAt().isBefore(LocalDateTime.now()) ||
                    visitStatus.equals(CANCELLED_BY_DOCTOR)) {
                visit.setStatus(visitStatus);
                visitRepository.save(visit);
            } else {
                throw new IllegalStateException("The time hasn't come");
            }
        }

    }

}
