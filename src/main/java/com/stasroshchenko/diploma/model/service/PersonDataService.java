package com.stasroshchenko.diploma.model.service;

import com.stasroshchenko.diploma.entity.person.ClientData;
import com.stasroshchenko.diploma.entity.person.DoctorData;
import com.stasroshchenko.diploma.model.repository.person.ClientDataRepository;
import com.stasroshchenko.diploma.model.repository.person.DoctorDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public boolean isClientTableHasPasswordId(Long passwordId) {
//        boolean isDoctorTableHasPasswordId =
//                getAllDoctors().stream()
//                        .anyMatch(doctorData -> doctorData.getPassportId().equals(passwordId));
//        boolean isClientTableHasPasswordId =
        return getAllClients().stream()
                        .anyMatch(clientData ->
                                clientData.getPassportId().equals(passwordId));

//        return isDoctorTableHasPasswordId || isClientTableHasPasswordId;
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

    public ClientData signUpClientData(ClientData clientData) {
        Optional<ClientData> clientOptional = getAllClients().stream()
                .filter(c -> c.equals(clientData))
                .findFirst();

        ClientData clientFromDB;

        if (clientOptional.isEmpty()) {
            clientFromDB = clientDataRepository.save(clientData);

        } else {
            clientFromDB = clientOptional.get();
        }

        return clientFromDB;

    }
}
