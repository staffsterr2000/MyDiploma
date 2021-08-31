package com.stasroshchenko.clinic.model.service;

import com.stasroshchenko.clinic.entity.ConfirmationToken;
import com.stasroshchenko.clinic.model.repository.ConfirmationTokenRepository;
import com.stasroshchenko.clinic.util.TokenHelper;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
class ConfirmationTokenServiceTest {

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @MockBean
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Test
    void shouldThrowExceptionIfAbsentTokenPassedInto() {
        String token = TokenHelper.createStringToken();

        Exception exception = Assert.assertThrows(
                IllegalStateException.class,
                () -> confirmationTokenService.getConfirmationToken(token)
        );

        assertThat(exception).hasMessage("No such token.");

    }

    @Test
    void shouldSetConfirmedAt() {
        ConfirmationToken confirmationToken = new ConfirmationToken();

        String token = TokenHelper.createStringToken();
        confirmationToken.setToken(token);

        Mockito.doReturn(Optional.of(confirmationToken))
                .when(confirmationTokenRepository)
                .findByToken(token);

        confirmationTokenService.setConfirmedAt(token);

        Mockito.verify(confirmationTokenRepository, Mockito.times(1))
                .findByToken(token);
        Assertions.assertNotNull(confirmationToken.getConfirmedAt());

    }
}