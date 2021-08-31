package com.stasroshchenko.clinic.model.service.user;

import com.stasroshchenko.clinic.entity.person.ClientData;
import com.stasroshchenko.clinic.entity.user.ApplicationUserClient;
import com.stasroshchenko.clinic.entity.user.ApplicationUserDoctor;
import com.stasroshchenko.clinic.entity.ConfirmationToken;
import com.stasroshchenko.clinic.entity.person.DoctorData;
import com.stasroshchenko.clinic.entity.user.ApplicationUser;
import com.stasroshchenko.clinic.model.repository.ApplicationUserRepository;
import com.stasroshchenko.clinic.model.service.ConfirmationTokenService;
import com.stasroshchenko.clinic.util.TokenHelper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Processes all user business logic.
 * @author staffsterr2000
 * @version 1.0
 */
@Service
@AllArgsConstructor
class ApplicationUserServiceReal implements UserDetailsService {

    /**
     * User repo
     */
    private final ApplicationUserRepository applicationUserRepository;

    /**
     * Password encoder
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Confirmation token service
     */
    private final ConfirmationTokenService confirmationTokenService;

    /**
     * Gets user by either its username or its email.
     * @param input username or email user entered in Login page.
     * @return required user.
     * @throws UsernameNotFoundException if user with such username
     * isn't found.
     * @since 1.0
     */
    @Override
    public ApplicationUser loadUserByUsername(String input) throws UsernameNotFoundException {
        // seeks by email
        Optional<ApplicationUser> optionalUser =
                applicationUserRepository.findByEmail(input);

        // if such user doesn't exist
        if (optionalUser.isEmpty()) {
            // seeks by username
            optionalUser = applicationUserRepository.findByUsername(input);

            // returns user by username or throws IllegalStateException
            return optionalUser.orElseThrow(() ->
                    new UsernameNotFoundException("User with such credentials doesn't exist"));
        } else {
            // returns user by email
            return optionalUser.get();
        }
    }



    /**
     * Gets user only by its username.
     * @param username username of the user we're looking for.
     * @return required user.
     * @throws IllegalStateException if user wasn't found.
     * @since 1.0
     */
    public ApplicationUser loadUserByOnlyUsername(String username) {
        // seeks by username
        return applicationUserRepository.findByUsername(username)
                // else throws exception
                .orElseThrow(() -> new IllegalStateException("User with " + username + " username doesn't exist"));
    }



    /**
     * Gets user only by its email.
     * @param email email of the user we're looking for.
     * @return required user.
     * @throws IllegalStateException if user wasn't found.
     * @since 1.0
     */
    public ApplicationUser loadUserByOnlyEmail(String email) {
        // seeks by email
        return applicationUserRepository.findByEmail(email)
                // else throws exception
                .orElseThrow(() -> new IllegalStateException("User with " + email + " email doesn't exist"));
    }



    /**
     * Gets user by its client data.
     * @param clientData client data of the user we're looking for.
     * @return required user.
     * @throws IllegalStateException if user wasn't found.
     * @since 1.0
     */
    public ApplicationUserClient loadUserByClientData(ClientData clientData) {
        // stream of all clients
        return loadAllUserClients().stream()
                // seek for client data matching
                .filter(client -> client.getClientData().equals(clientData))
                // find first
                .findFirst()
                // else throw exception
                .orElseThrow(() -> new IllegalStateException("User with " + clientData + " data doesn't exist"));
    }



    /**
     * Gets user by its doctor data.
     * @param doctorData doctor data of the user we're looking for.
     * @return required user.
     * @throws IllegalStateException if user wasn't found.
     * @since 1.0
     */
    public ApplicationUserDoctor loadUserByDoctorData(DoctorData doctorData) {
        // stream of all doctors
        return loadAllUserDoctors().stream()
                // seek for doctor data matching
                .filter(doctor -> doctor.getDoctorData().equals(doctorData))
                // take first matched object from stream
                .findFirst()
                // else throw exception
                .orElseThrow(() -> new IllegalStateException("User with " + doctorData + " data doesn't exist"));
    }



    /**
     * Gets list of all client users.
     * @return list of all client users.
     * @since 1.0
     */
    public List<ApplicationUserClient> loadAllUserClients() {
        // stream of all users
        return applicationUserRepository.findAll().stream()
                // seek for only client user instances
                .filter(user -> user instanceof ApplicationUserClient)
                // cast
                .map(user -> (ApplicationUserClient) user)
                // collect to list
                .collect(Collectors.toList());
    }



    /**
     * Gets list of all doctor users.
     * @return list of all doctor users.
     * @since 1.0
     */
    public List<ApplicationUserDoctor> loadAllUserDoctors() {
        // stream of all users
        return applicationUserRepository.findAll().stream()
                // seek for only doctor users
                .filter(user -> user instanceof ApplicationUserDoctor)
                // cast
                .map(user -> (ApplicationUserDoctor) user)
                // collect the users to list
                .collect(Collectors.toList());
    }



    /**
     * Checks for username and email absence in DB.
     * @param rawUser raw user without password encoding.
     * @return bool of the absence (true - absent).
     * @since 1.0
     */
    public boolean isUsernameAndEmailAbsent(ApplicationUser rawUser) {
        try {
            // checks
            checkUsernameAndEmailPresence(rawUser);
            // if no exception is thrown, returns true
            return true;
        } catch (IllegalStateException ex) {
            // if the exception is caught, returns false
            return false;
        }
    }



    /**
     * Checks for username and email absence in DB. Causes
     * exception if user with such data is present.
     * @param rawUser applicationUser to check is this rawUser is in the base
     * @return rawUser if this user is not in the base
     * @throws IllegalStateException if this username/email has already been taken
     * @since 1.0
     */
    public ApplicationUser checkUsernameAndEmailPresence(ApplicationUser rawUser) {
        String userUsername = rawUser.getUsername();
        String userEmail = rawUser.getEmail();

        // seek for email
        Optional<ApplicationUser> userByEmail =
                applicationUserRepository.findByEmail(userEmail);
        // seek for username
        Optional<ApplicationUser> userByUsername =
                applicationUserRepository.findByUsername(userUsername);

        // if present both
        if (userByEmail.isPresent() && userByUsername.isPresent()) {
            throw new IllegalStateException(
                    String.format("Email %s and username %s has already been taken", userEmail, userUsername)
            );
        }

        // if present only username
        if (userByUsername.isPresent()) {
            throw new IllegalStateException(
                    String.format("Username %s has already been taken", userUsername)
            );
        }

        // if present only email
        if (userByEmail.isPresent()) {
            throw new IllegalStateException(
                    String.format("Email %s has already been taken", userEmail)
            );
        }

        // if nothing present
        return rawUser;
    }



    /**
     * Seeks the user by its email and enables the user.
     * @param email user's email by which user will be found
     *              and then the user will be enabled.
     * @since 1.0
     */
    public void enableUserByEmail(String email) {
        loadUserByOnlyEmail(email)      // seek by email
                .setEnabled(true);      // enable
    }



    /**
     * Encodes the user instance and signs it up.
     * @param rawUser raw user without password encoding.
     * @return confirmation token UUID.
     * @since 1.0
     */
    public String signUpUser(ApplicationUser rawUser) {
        // checks email & username for presence
        ApplicationUser user = checkUsernameAndEmailPresence(rawUser);

        // encodes raw password
        String encodedPassword = passwordEncoder
                .encode(user.getPassword());
        user.setPassword(encodedPassword);

        // saves user
        applicationUserRepository.save(user);

        // creates confirmation token
        ConfirmationToken confirmationToken =
                TokenHelper.createConfirmationToken(user);
        String token = confirmationToken.getToken();
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        // returns token UUID
        return token;
    }



    /**
     * Deletes old one token, creates new one and returns it.
     * @param user user who needs new token.
     * @return new confirmation token UUID.
     * @since 1.0
     */
    public String resendConfirmationToken(ApplicationUser user) {
        // seek for old one
        ConfirmationToken unconfirmedToken =
                // stream of all tokens
                confirmationTokenService.getAllConfirmationTokens().stream()
                        // seek for token linked to this user
                        .filter(confirmationToken ->
                                rawUserAndEncodedUserEquality(user, confirmationToken.getApplicationUser()))
                        // seek for unconfirmed token
                        .filter(confirmationToken ->
                                confirmationToken.getConfirmedAt() == null)
                        // find first such token
                        .findFirst()
                        // else throw exception
                        .orElseThrow(() -> new IllegalStateException("No such user in database"));

        ApplicationUser userFromDB = unconfirmedToken.getApplicationUser();

        // create new token
        ConfirmationToken newConfirmationToken =
                TokenHelper.createConfirmationToken(userFromDB);

        // delete previous token
        confirmationTokenService.deleteConfirmationToken(unconfirmedToken);

        // save new token
        confirmationTokenService.saveConfirmationToken(newConfirmationToken);

        // return token UUID
        return newConfirmationToken.getToken();
    }



    /**
     * Checks do raw user and encoded user match each other.
     * @param rawUser raw user.
     * @param encodedUser encoded user.
     * @return bool of users' equality (true - equal)
     * @since 1.0
     */
    private boolean rawUserAndEncodedUserEquality(
            ApplicationUser rawUser,ApplicationUser encodedUser) {
        return
                // compare by email
                rawUser.getEmail().equals(encodedUser.getEmail()) &&
                        // compare by username
                        rawUser.getUsername().equals(encodedUser.getUsername()) &&
                        // compare by password
                        passwordEncoder.matches(rawUser.getPassword(), encodedUser.getPassword());
    }

}
