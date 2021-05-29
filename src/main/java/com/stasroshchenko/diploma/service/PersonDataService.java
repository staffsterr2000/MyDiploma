package com.stasroshchenko.diploma.service;

import com.stasroshchenko.diploma.entity.database.person.ClientData;
import com.stasroshchenko.diploma.entity.database.person.DoctorData;
import com.stasroshchenko.diploma.entity.database.person.PersonData;
import com.stasroshchenko.diploma.repository.person.ClientDataRepository;
import com.stasroshchenko.diploma.repository.person.DoctorDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PersonDataService {

    private final DoctorDataRepository doctorDataRepository;
    private final ClientDataRepository clientDataRepository;

    public List<DoctorData> getAllDoctors() {
        return doctorDataRepository.findAll();
    }

    public List<ClientData> getAllClients() {
        return clientDataRepository.findAll();
    }

    public ClientData getClientById(Long id) {
        return clientDataRepository.findAll().stream()
                .filter(personData -> id.equals(personData.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Person with id " + id + " doesn't exist"));
    }

    public DoctorData getDoctorById(Long id) {
        return doctorDataRepository.findAll().stream()
                .filter(personData -> id.equals(personData.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Person with id " + id + " doesn't exist"));
    }

}
