package com.stasroshchenko.diploma.service;

import com.stasroshchenko.diploma.entity.database.Visit;
import com.stasroshchenko.diploma.entity.database.person.ClientData;
import com.stasroshchenko.diploma.entity.database.person.DoctorData;
import com.stasroshchenko.diploma.entity.database.person.PersonData;
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

    public List<Visit> getAllVisitsByPerson(PersonData personData) {
        if (personData instanceof ClientData) {
            return visitRepository.findByClientData((ClientData) personData);
        }

        if (personData instanceof DoctorData) {
            return visitRepository.findByDoctorData((DoctorData) personData);
        }

        throw new IllegalStateException("Unknown type");
    }

    public void saveVisit(Visit visit) {
        visitRepository.save(visit);
    }

}
