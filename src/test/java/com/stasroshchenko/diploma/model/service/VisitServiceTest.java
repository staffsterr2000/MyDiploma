package com.stasroshchenko.diploma.model.service;

import com.stasroshchenko.diploma.entity.Visit;
import com.stasroshchenko.diploma.entity.person.ClientData;
import com.stasroshchenko.diploma.entity.person.DoctorData;
import com.stasroshchenko.diploma.entity.user.ApplicationUserClient;
import com.stasroshchenko.diploma.entity.user.ApplicationUserDoctor;
import com.stasroshchenko.diploma.model.repository.VisitRepository;
import com.stasroshchenko.diploma.request.visit.AcceptVisitRequest;
import com.stasroshchenko.diploma.request.visit.CreateVisitRequest;
import com.stasroshchenko.diploma.request.visit.PassVisitRequest;
import com.stasroshchenko.diploma.request.visit.SendVisitRequest;
import com.stasroshchenko.diploma.util.VisitStatus;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class VisitServiceTest {

    @MockBean
    private PersonDataService personDataService;

    @MockBean
    private VisitRepository visitRepository;

    @Autowired
    private VisitService visitService;

    @Test
    void shouldThrowExceptionIfVisitAlreadySentOrActive() {
        DoctorData doctorData = new DoctorData();
        doctorData.setId(1L);

        ClientData clientData = new ClientData();

        Visit visit = new Visit();
        visit.setStatus(VisitStatus.SENT);
        visit.setDoctorData(doctorData);
        visit.setClientData(clientData);

        Mockito.doReturn(Lists.newArrayList(visit))
                .when(visitRepository)
                .findAllOrdered();

        Mockito.doReturn(doctorData)
                .when(personDataService)
                .getDoctorById(1L);

        SendVisitRequest sendVisitRequest = new SendVisitRequest();
        sendVisitRequest.setDoctorDataId(1L);
        Assertions.assertThrows(IllegalStateException.class,
                () -> visitService.sendVisit(clientData, sendVisitRequest));

        CreateVisitRequest createVisitRequest = new CreateVisitRequest();
        Assertions.assertThrows(IllegalStateException.class,
                () -> visitService.createVisit(doctorData, createVisitRequest));

    }

    @Test
    void shouldThrowExceptionIfAppointmentTimeIsInvalid() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        DoctorData doctorData = new DoctorData();
        ClientData clientData = new ClientData();

        Visit visit = new Visit();
        visit.setStatus(VisitStatus.SENT);
        visit.setDoctorData(doctorData);
        visit.setClientData(clientData);
        visit.setAppointsAt(LocalDateTime.of(tomorrow, LocalTime.of(11, 0)));

        Mockito.doReturn(Lists.newArrayList(visit))
                .when(visitRepository)
                .findAllOrdered();

        AcceptVisitRequest acceptVisitRequest = new AcceptVisitRequest();
        CreateVisitRequest createVisitRequest = new CreateVisitRequest();

        // earlier than workday starts
        acceptVisitRequest.setAppointsAt(LocalDateTime.of(tomorrow, LocalTime.of(7, 59)));
        Assertions.assertThrows(IllegalStateException.class,
                () -> visitService.acceptVisit(doctorData, acceptVisitRequest));
        createVisitRequest.setAppointsAt(LocalDateTime.of(tomorrow, LocalTime.of(7, 59)));
        Assertions.assertThrows(IllegalStateException.class,
                () -> visitService.createVisit(doctorData, createVisitRequest));


        // later than last session could be
        acceptVisitRequest.setAppointsAt(LocalDateTime.of(tomorrow, LocalTime.of(17, 1)));
        Assertions.assertThrows(IllegalStateException.class,
                () -> visitService.acceptVisit(doctorData, acceptVisitRequest));
        createVisitRequest.setAppointsAt(LocalDateTime.of(tomorrow, LocalTime.of(17, 1)));
        Assertions.assertThrows(IllegalStateException.class,
                () -> visitService.createVisit(doctorData, createVisitRequest));


        // not tomorrow, but with valid time
        acceptVisitRequest.setAppointsAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0)));
        Assertions.assertThrows(IllegalStateException.class,
                () -> visitService.acceptVisit(doctorData, acceptVisitRequest));
        createVisitRequest.setAppointsAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0)));
        Assertions.assertThrows(IllegalStateException.class,
                () -> visitService.createVisit(doctorData, createVisitRequest));


        // this time has already been taken
        acceptVisitRequest.setAppointsAt(LocalDateTime.of(tomorrow, LocalTime.of(11, 0)));
        Assertions.assertThrows(IllegalStateException.class,
                () -> visitService.acceptVisit(doctorData, acceptVisitRequest));
        createVisitRequest.setAppointsAt(LocalDateTime.of(tomorrow, LocalTime.of(11, 0)));
        Assertions.assertThrows(IllegalStateException.class,
                () -> visitService.createVisit(doctorData, createVisitRequest));

    }

    @Test
    void shouldThrowExceptionIfItIsEarlyToMakeVisitEitherOccurredOrNonOccurred() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDateTime tomorrowMorning = LocalDateTime
                .of(tomorrow, LocalTime.of(9, 0));

        DoctorData doctorData = new DoctorData();
        ClientData clientData = new ClientData();

        Visit visit = new Visit();
        visit.setId(1L);
        visit.setStatus(VisitStatus.ACTIVE);
        visit.setDoctorData(doctorData);
        visit.setClientData(clientData);
        visit.setAppointsAt(tomorrowMorning);

        Mockito.doReturn(Optional.of(visit))
                .when(visitRepository)
                .findById(visit.getId());

        PassVisitRequest passVisitRequest = new PassVisitRequest();

        passVisitRequest.setStatus(VisitStatus.OCCURRED);
        Assertions.assertThrows(IllegalStateException.class,
                () -> visitService.passVisit(doctorData, passVisitRequest));

        passVisitRequest.setStatus(VisitStatus.NOT_OCCURRED);
        Assertions.assertThrows(IllegalStateException.class,
                () -> visitService.passVisit(doctorData, passVisitRequest));
    }

    @Test
    void declineVisit() {
    }

    @Test
    void cancelVisit() {
    }

    @Test
    void passVisit() {
    }

}