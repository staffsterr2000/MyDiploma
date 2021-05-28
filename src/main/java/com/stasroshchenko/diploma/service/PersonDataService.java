package com.stasroshchenko.diploma.service;

import com.stasroshchenko.diploma.entity.database.person.ClientData;
import com.stasroshchenko.diploma.entity.database.person.DoctorData;
import com.stasroshchenko.diploma.repository.ClientDataRepository;
import com.stasroshchenko.diploma.repository.DoctorDataRepository;
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

}
