package com.stasroshchenko.clinic.model.service;

import com.stasroshchenko.clinic.entity.person.ClientData;
import com.stasroshchenko.clinic.model.repository.person.ClientDataRepository;
import com.stasroshchenko.clinic.entity.person.DoctorData;
import com.stasroshchenko.clinic.model.repository.person.DoctorDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Processes all person data business logic.
 * @author staffsterr2000
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class PersonDataService {

    /**
     * Doctor repo
     */
    private final DoctorDataRepository doctorDataRepository;

    /**
     * Client repo
     */
    private final ClientDataRepository clientDataRepository;



    /**
     * Gets list of all doctors.
     * @return list of all doctors.
     * @since 1.0
     */
    public List<DoctorData> getAllDoctors() {
        return doctorDataRepository.findAll();
    }



    /**
     * Gets list of all clients.
     * @return list of all clients.
     * @since 1.0
     */
    public List<ClientData> getAllClients() {
        return clientDataRepository.findAll();
    }



    /**
     * Gets doctor by his id.
     * @param id id of the doctor we're looking for.
     * @return required doctor.
     * @throws IllegalStateException if doctor with such id isn't found.
     * @since 1.0
     */
    public DoctorData getDoctorById(Long id) {
        // stream of all doctors
        return getAllDoctors().stream()
                // seek for the same id
                .filter(personData -> id.equals(personData.getId()))
                // find first
                .findFirst()
                // else throw exception
                .orElseThrow(() -> new IllegalStateException("Person with id " + id + " doesn't exist"));
    }



    /**
     * Gets client by his id.
     * @param id id of the client we're looking for.
     * @return required client.
     * @throws IllegalStateException if client with such id isn't found.
     * @since 1.0
     */
    public ClientData getClientById(Long id) {
        // stream of all clients
        return getAllClients().stream()
                // seek for the same id
                .filter(personData -> id.equals(personData.getId()))
                // find first
                .findFirst()
                // else throw exception
                .orElseThrow(() -> new IllegalStateException("Person with id " + id + " doesn't exist"));
    }



    /**
     * Checks that the passport ID doesn't exist in the DB.
     * @param passwordId the passport ID.
     * @return bool result of checking (true - at least one other client
     * has the same ID).
     * @since 1.0
     */
    public boolean isAnyClientHasSuchPassportId(Long passwordId) {
        return getAllClients().stream()
                .anyMatch(clientData ->
                        clientData.getPassportId().equals(passwordId));
    }



    /**
     * Signs the client data up.
     * @param clientData the client data to sign up.
     * @return client data.
     * @since 1.0
     */
    public ClientData signUpClientData(ClientData clientData) {
        // seek for existing client
        Optional<ClientData> clientOptional =
                getAllClients().stream()
                        .filter(c -> c.equals(clientData))
                        .findFirst();

        ClientData clientFromDB;

        // if such client absent
        if (clientOptional.isEmpty()) {
            // sign up new one
            clientFromDB = clientDataRepository.save(clientData);
        } else {
            // else get already present client
            clientFromDB = clientOptional.get();
        }

        // return the client
        return clientFromDB;

    }

}
