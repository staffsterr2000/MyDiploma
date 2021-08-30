package com.stasroshchenko.diploma.model.service;

import com.stasroshchenko.diploma.entity.person.ClientData;
import com.stasroshchenko.diploma.model.repository.person.ClientDataRepository;
import com.stasroshchenko.diploma.util.PassportIdGenerator;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class PersonDataServiceTest {

    @Autowired
    private PersonDataService personDataService;

    @MockBean
    private ClientDataRepository clientDataRepository;

    @Test
    void shouldNotSaveExistingClient() {
        ClientData clientData = new ClientData();

        Mockito.doReturn(Lists.newArrayList(clientData))
                .when(clientDataRepository)
                .findAll();

        personDataService.signUpClientData(clientData);

        Mockito.verify(clientDataRepository, Mockito.times(0))
                .save(clientData);

    }


    @Test
    void shouldReturnTrueIfGivenPassportIdAlreadyExists() {
        ClientData clientData = new ClientData();

        Long passportId = PassportIdGenerator.generatePassportId();

        clientData.setPassportId(passportId);

        Mockito.doReturn(Lists.newArrayList(clientData))
                .when(clientDataRepository)
                .findAll();

        boolean answer = personDataService.isAnyClientHasSuchPassportId(passportId);

        Assertions.assertTrue(answer);

    }
}