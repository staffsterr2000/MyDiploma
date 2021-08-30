package com.stasroshchenko.diploma.model.service;

import com.stasroshchenko.diploma.entity.ConfirmationToken;
import com.stasroshchenko.diploma.entity.person.ClientData;
import com.stasroshchenko.diploma.entity.user.ApplicationUserClient;
import com.stasroshchenko.diploma.model.service.email.EmailSender;
import com.stasroshchenko.diploma.model.service.user.ApplicationUserService;
import com.stasroshchenko.diploma.request.RegistrationRequest;
import com.stasroshchenko.diploma.util.TokenHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
class RegistrationServiceTest {

    @Autowired
    private RegistrationService registrationService;

    @MockBean
    private EmailSender emailSender;

    @MockBean
    private ApplicationUserService applicationUserService;

    @MockBean
    private ConfirmationTokenService confirmationTokenService;

    @Test
    void shouldCallUserServiceAndSendEmail() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("ro33@gmail.com");
        request.setUsername("ro33");

        ApplicationUserClient rawUser =
                new ApplicationUserClient(
                        new ClientData(
                                request.getFirstName(),
                                request.getLastName(),
                                request.getDateOfBirth(),
                                request.getPassportId()
                        ),
                        request.getUsername(),
                        request.getEmail(),
                        request.getPassword()
                );

        registrationService.signUpUser(request);


        Mockito.verify(applicationUserService, Mockito.times(1))
                .signUpUser(rawUser);

        Mockito.verify(emailSender, Mockito.times(1)).
                send(
                        ArgumentMatchers.eq(request.getEmail()),
                        ArgumentMatchers.anyString()
                );

    }

    @Test
    void shouldThrowExceptionAboutConfirmationDate() {
        ConfirmationToken confirmationToken = new ConfirmationToken();

        String token = TokenHelper.createStringToken();
        confirmationToken.setToken(token);

        LocalDateTime localDateTime = LocalDateTime.now();
        confirmationToken.setConfirmedAt(localDateTime);

        Mockito.doReturn(confirmationToken)
                .when(confirmationTokenService)
                .getConfirmationToken(token);

        Assertions.assertThrows(
                IllegalStateException.class,
                () -> registrationService.confirmToken(token)
        );

    }

    @Test
    void shouldThrowExceptionAboutExpiration() {
        ConfirmationToken confirmationToken = new ConfirmationToken();

        String token = TokenHelper.createStringToken();
        confirmationToken.setToken(token);

        LocalDateTime localDateTime = LocalDateTime.now()
                .minusMinutes(TokenHelper.TOKEN_EXPIRE_TIMEOUT_MINUTES + 5);
        confirmationToken.setExpiresAt(localDateTime);

        Mockito.doReturn(confirmationToken)
                .when(confirmationTokenService)
                .getConfirmationToken(token);

        Assertions.assertThrows(
                IllegalStateException.class,
                () -> registrationService.confirmToken(token)
        );
    }

    @Test
    void shouldCallUserServiceAndResendEmail() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("ro33@gmail.com");
        request.setUsername("ro33");

        ApplicationUserClient rawUser =
                new ApplicationUserClient(
                        new ClientData(
                                request.getFirstName(),
                                request.getLastName(),
                                request.getDateOfBirth(),
                                request.getPassportId()
                        ),
                        request.getUsername(),
                        request.getEmail(),
                        request.getPassword()
                );

        registrationService.resendConfirmationToken(request);

        Mockito.verify(applicationUserService, Mockito.times(1))
                .resendConfirmationToken(rawUser);

        Mockito.verify(emailSender, Mockito.times(1)).
                send(
                        ArgumentMatchers.eq(request.getEmail()),
                        ArgumentMatchers.anyString()
                );

    }

}