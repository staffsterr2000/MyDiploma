package com.stasroshchenko.diploma.model.service;

import com.stasroshchenko.diploma.entity.person.ClientData;
import com.stasroshchenko.diploma.entity.user.ApplicationUser;
import com.stasroshchenko.diploma.entity.user.ApplicationUserClient;
import com.stasroshchenko.diploma.model.service.user.ApplicationUserService;
import com.stasroshchenko.diploma.model.service.email.EmailSender;
import com.stasroshchenko.diploma.request.RegistrationRequest;
import com.stasroshchenko.diploma.entity.ConfirmationToken;
import com.stasroshchenko.diploma.util.EmailMessageHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.LocalDateTime;

/**
 * Processes all registration business logic.
 * @author staffsterr2000
 * @version 1.0
 */
@Service
@AllArgsConstructor
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
        String host = "localhost:8080";
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
        String host = "localhost:8080";
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
