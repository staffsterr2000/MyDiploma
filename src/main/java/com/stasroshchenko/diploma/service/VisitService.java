package com.stasroshchenko.diploma.service;

import com.stasroshchenko.diploma.entity.database.Visit;
import com.stasroshchenko.diploma.entity.database.person.ClientData;
import com.stasroshchenko.diploma.entity.database.person.DoctorData;
import com.stasroshchenko.diploma.repository.VisitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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

    public void saveVisit(Visit visit) {
        visitRepository.save(visit);
    }

}
