package com.stasroshchenko.clinic.model.service;

import com.stasroshchenko.clinic.entity.ConfirmationToken;
import com.stasroshchenko.clinic.entity.person.ClientData;
import com.stasroshchenko.clinic.entity.user.ApplicationUser;
import com.stasroshchenko.clinic.entity.user.ApplicationUserClient;
import com.stasroshchenko.clinic.model.service.email.EmailSender;
import com.stasroshchenko.clinic.model.service.user.ApplicationUserService;
import com.stasroshchenko.clinic.request.RegistrationRequest;
import com.stasroshchenko.clinic.util.EmailMessageHelper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.LocalDateTime;

/**
 * Processes all registration business logic.
 * @author staffsterr2000
 * @version 1.0
 */
@Service
public class RegistrationService {

    /**
     * User service
     */
    private final ApplicationUserService applicationUserService;

    /**
     * Email sender
     */
    private final EmailSender emailSender;

    /**
     * Token service
     */
    private final ConfirmationTokenService confirmationTokenService;


    /**
     * Host name
     */
    private final String host;



    @Autowired
    public RegistrationService(
            ApplicationUserService applicationUserService,
            EmailSender emailSender,
            ConfirmationTokenService confirmationTokenService,
            @Value("${custom.host}") String host) {
        this.applicationUserService = applicationUserService;
        this.emailSender = emailSender;
        this.confirmationTokenService = confirmationTokenService;
        this.host = host;
    }



    /**
     * Converts registration request, signs up user and sends him an
     * email with confirmation link.
     * @param request registration request
     * @since 1.0
     */
    public void signUpUser(RegistrationRequest request) {
        String requestEmail = request.getEmail();

        // sign up user through user service and get token UUID
        String token = applicationUserService.signUpUser(
                // convert request to client user
                convertRegistrationRequestToApplicationUser(request));

        // create confirmation link
        String link = "http://" + host + "/registration/confirm?token=" + token;

        // send email with link to the user's email address
        emailSender.send(requestEmail,
                // build email message
                EmailMessageHelper.buildEmailMessage(request.getUsername(), link));

    }



    /**
     * Checks for both confirmation and expiration time, then confirms
     * the token and enables the user.
     * @param token UUID of the token.
     * @since 1.0
     */
    @Transactional
    public void confirmToken(String token) {
        // get token by UUID
        ConfirmationToken confirmationToken =
                confirmationTokenService.getConfirmationToken(token);

        // if token has already been confirmed - throw exception
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email has already been confirmed");
        }

        LocalDateTime expiresAt = confirmationToken.getExpiresAt();

        // if token has already expired - throw exception
        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token has been expired");
        }

        // else confirm token by UUID
        confirmationTokenService.setConfirmedAt(token);

        // and enable user by email
        applicationUserService.enableUserByEmail(
                confirmationToken.getApplicationUser().getEmail());

    }



    /**
     * Converts registration request, deletes the old confirmation token,
     * creates new one and sends it to the user's email address.
     * @param request registration request.
     * @since 1.0
     */
    public void resendConfirmationToken(RegistrationRequest request) {
        String requestEmail = request.getEmail();

        // delete old token and create new through user service
        // and get token UUID
        String token = applicationUserService.resendConfirmationToken(
                // convert request to client user
                convertRegistrationRequestToApplicationUser(request));

        // create confirmation link
        String link = "http://" + host + "/registration/confirm?token=" + token;
        // send email with link to the user's email address
        emailSender.send(requestEmail,
                // build email message
                EmailMessageHelper.buildEmailMessage(request.getUsername(), link));

    }



    /**
     * Converts {@link RegistrationRequest} to {@link ApplicationUserClient}.
     * @param request registration request.
     * @return converted raw user.
     * @since 1.0
     */
    private ApplicationUser convertRegistrationRequestToApplicationUser(
            RegistrationRequest request) {

        return new ApplicationUserClient(
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

    }

}
