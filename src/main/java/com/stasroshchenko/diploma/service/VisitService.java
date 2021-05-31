package com.stasroshchenko.diploma.service;

import com.stasroshchenko.diploma.entity.database.Visit;
import com.stasroshchenko.diploma.entity.database.person.ClientData;
import com.stasroshchenko.diploma.entity.database.person.DoctorData;
import com.stasroshchenko.diploma.repository.VisitRepository;
import com.stasroshchenko.diploma.util.VisitStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;

    public List<Visit> getAllVisits() {
        return visitRepository.findAll();
    }

    public List<Visit> getAllVisitsByClient(ClientData clientData) {
        return visitRepository.findByClientData(clientData);
    }

    public List<Visit> getAllVisitsByDoctor(DoctorData doctorData) {
        return visitRepository.findByDoctorData(doctorData);
    }

    public Visit getVisitById(Long id) {
        return visitRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(String.format("Visit with id %d wasn't found", id)));
    }

    public void saveVisit(Visit visit) {
        visitRepository.save(visit);
    }

    public boolean isTimeFree(LocalDateTime visitDate) {
        return visitRepository.findAll().stream()
                .filter(visit -> visit.getStatus().equals(VisitStatus.ACTIVE))
                .filter(visit -> {
                    LocalDateTime appointsAt = visit.getAppointsAt();
                    return (visitDate.isBefore(appointsAt) || visitDate.isAfter(appointsAt.plusHours(1)));
                })
                .findAny()
                .isEmpty();
    }

}
