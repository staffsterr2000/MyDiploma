package com.stasroshchenko.clinic.model.service.user;

import com.stasroshchenko.clinic.entity.ConfirmationToken;
import com.stasroshchenko.clinic.entity.person.ClientData;
import com.stasroshchenko.clinic.entity.person.DoctorData;
import com.stasroshchenko.clinic.entity.user.ApplicationUser;
import com.stasroshchenko.clinic.entity.user.ApplicationUserClient;
import com.stasroshchenko.clinic.entity.user.ApplicationUserDoctor;
import com.stasroshchenko.clinic.model.repository.ApplicationUserRepository;
import com.stasroshchenko.clinic.model.service.ConfirmationTokenService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
class ApplicationUserServiceRealTest {

    @MockBean
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private ApplicationUserServiceReal applicationUserServiceReal;

    @Test
    void shouldThrowExceptionIfNeitherEmailNorUsernameExist() {
        String input = "lobanov_semen";

        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> applicationUserServiceReal.loadUserByUsername(input));
    }

    @Test
    void shouldReturnUserByEitherEmailOrUsername() {
        ApplicationUser doctorByEmail = new ApplicationUserDoctor(
                new DoctorData(),
                "garikk2001",
                "garikk2001@gmail.com",
                "password"
        );

        ApplicationUser doctorByUsername = new ApplicationUserDoctor(
                new DoctorData(),
                "kiki_rill",
                "kiki_rill@gmail.com",
                "password"
        );

        Mockito.doReturn(Optional.of(doctorByEmail))
                .when(applicationUserRepository)
                .findByEmail(doctorByEmail.getEmail());
        Mockito.doReturn(Optional.of(doctorByUsername))
                .when(applicationUserRepository)
                .findByUsername(doctorByUsername.getUsername());

        ApplicationUser returnedDoctorByEmail = applicationUserServiceReal
                .loadUserByUsername(doctorByEmail.getEmail());
        ApplicationUser returnedDoctorByUsername = applicationUserServiceReal
                .loadUserByUsername(doctorByUsername.getUsername());

        Assertions.assertSame(doctorByEmail, returnedDoctorByEmail);
        Assertions.assertSame(doctorByUsername, returnedDoctorByUsername);

    }

    @Test
    void shouldReturnUserByPersonData() {
        ApplicationUserDoctor doctor = new ApplicationUserDoctor();
        DoctorData doctorData = new DoctorData();
        doctor.setDoctorData(doctorData);

        ApplicationUserClient client = new ApplicationUserClient();
        ClientData clientData = new ClientData();
        client.setClientData(clientData);

        Mockito.doReturn(Lists.newArrayList(doctor, client))
                .when(applicationUserRepository)
                .findAll();

        ApplicationUser returnedDoctor = applicationUserServiceReal
                .loadUserByDoctorData(doctorData);
        ApplicationUser returnedClient = applicationUserServiceReal
                .loadUserByClientData(clientData);

        Assertions.assertSame(doctor, returnedDoctor);
        Assertions.assertSame(client, returnedClient);
    }

    @Test
    void shouldThrowExceptionIfCredentialsHaveAlreadyBeenUsed() {
        ApplicationUser doctor = new ApplicationUserDoctor(
                new DoctorData(),
                "garikk2001",
                "garikk2001@gmail.com",
                "password"
        );

        ApplicationUser doctorWithUsernameChanged = new ApplicationUserDoctor(
                new DoctorData(),
                "",
                "garikk2001@gmail.com",
                "password"
        );

        ApplicationUser doctorWithEmailChanged = new ApplicationUserDoctor(
                new DoctorData(),
                "garikk2001",
                "",
                "password"
        );

        Mockito.doReturn(Optional.of(doctor))
                .when(applicationUserRepository)
                .findByUsername(doctor.getUsername());
        Mockito.doReturn(Optional.of(doctor))
                .when(applicationUserRepository)
                .findByEmail(doctor.getEmail());

        // email and username have already been taken exception
        Assertions.assertThrows(IllegalStateException.class,
                () -> applicationUserServiceReal.checkUsernameAndEmailPresence(doctor));

        // email has already been taken exception
        Assertions.assertThrows(IllegalStateException.class,
                () -> applicationUserServiceReal.checkUsernameAndEmailPresence(doctorWithUsernameChanged));

        // username has already been taken exception
        Assertions.assertThrows(IllegalStateException.class,
                () -> applicationUserServiceReal.checkUsernameAndEmailPresence(doctorWithEmailChanged));

    }

    @Test
    void shouldEncodeAndSaveBothUserAndConfirmationToken() {
        String rawPassword = "password";
        ApplicationUser doctor = new ApplicationUserDoctor(
                new DoctorData(),
                "garikk2001",
                "garikk2001@gmail.com",
                rawPassword
        );

        String token = applicationUserServiceReal.signUpUser(doctor);

        Assertions.assertTrue(passwordEncoder.matches(rawPassword, doctor.getPassword()));
        Mockito.verify(applicationUserRepository, Mockito.times(1))
                .save(doctor);

        ArgumentCaptor<ConfirmationToken> captor = ArgumentCaptor
                .forClass(ConfirmationToken.class);

        Mockito.verify(confirmationTokenService, Mockito.times(1))
                .saveConfirmationToken(captor.capture());
        Assertions.assertEquals(token, captor.getValue().getToken());

    }

}