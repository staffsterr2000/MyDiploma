package com.stasroshchenko.diploma.service;

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
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;

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
//        return visitRepository.findByClientData(clientData);
    }

    public List<Visit> getAllVisitsByDoctorOrdered(DoctorData doctorData) {
        return getAllVisitsOrdered().stream()
                .filter(visit -> visit.getDoctorData().equals(doctorData))
                .collect(Collectors.toList());
//        return visitRepository.findByDoctorData(doctorData);
    }

    public Visit getVisitById(Long id) {
        return visitRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(String.format("Visit with id %d wasn't found", id)));
    }

    public void saveVisit(Visit visit) {
        if (visit.getComplaint().isEmpty())
            throw new IllegalStateException("complaint field must be not blank");

        visitRepository.save(visit);
    }

    public boolean isTimeFree(LocalDateTime visitDate) {
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

    public boolean isDateValid(LocalDateTime visitDate) {
        if (visitDate == null) return false;

        LocalTime timeWorkdayStarts = LocalTime.of(8, 0);
        LocalTime timeWorkdayEnds = LocalTime.of(18, 0);
        LocalDate today = LocalDate.now();

        return visitDate.isAfter(LocalDateTime.of(today, timeWorkdayStarts)) &&
                visitDate.toLocalTime().isAfter(timeWorkdayStarts) &&
                visitDate.toLocalTime().isBefore(timeWorkdayEnds);
    }

    public void declineVisit(Long visitId, DoctorData doctorData) {
        Visit visit = getVisitById(visitId);

        if (visit.getStatus().equals(VisitStatus.SENT) &&
                doctorData.equals(visit.getDoctorData())) {

            visit.setStatus(VisitStatus.CANCELLED);
            visitRepository.save(visit);
        }

    }

    public void passVisit(Long visitId, VisitStatus visitStatus, DoctorData doctorData) {
        Visit visit = getVisitById(visitId);

        switch (visitStatus) {
            case OCCURRED:
            case NOT_OCCURRED:
            case CANCELLED:
                break;
            default:
                throw new IllegalStateException("Status " + visitStatus + " is wrong.");
        }

        if (visit.getStatus().equals(VisitStatus.ACTIVE) &&
//                visit.getAppointsAt().isBefore(LocalDateTime.now()) &&
                doctorData.equals(visit.getDoctorData())) {

            visit.setStatus(visitStatus);
            visitRepository.save(visit);
        }

    }

}
