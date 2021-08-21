package com.stasroshchenko.diploma.model.service.user;

import com.stasroshchenko.diploma.entity.ConfirmationToken;
import com.stasroshchenko.diploma.entity.person.ClientData;
import com.stasroshchenko.diploma.entity.person.DoctorData;
import com.stasroshchenko.diploma.entity.user.ApplicationUser;
import com.stasroshchenko.diploma.entity.user.ApplicationUserClient;
import com.stasroshchenko.diploma.entity.user.ApplicationUserDoctor;
import com.stasroshchenko.diploma.model.repository.ApplicationUserRepository;
import com.stasroshchenko.diploma.model.service.ConfirmationTokenService;
import com.stasroshchenko.diploma.util.TokenHelper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class ApplicationUserServiceReal implements UserDetailsService {

    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public ApplicationUser loadUserByUsername(String input) throws UsernameNotFoundException {
        Optional<ApplicationUser> optionalUser =
                applicationUserRepository.findByEmail(input);
        if (optionalUser.isEmpty()) {
            optionalUser = applicationUserRepository.findByUsername(input);
            return optionalUser.orElseThrow(() ->
                    new UsernameNotFoundException("User with such credentials doesn't exist"));
        } else {
            return optionalUser.get();
        }
    }

    public ApplicationUser loadUserByOnlyUsername(String username) {
        return applicationUserRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User with " + username + " username doesn't exist"));
    }

    public ApplicationUser loadUserByOnlyEmail(String email) {
        return applicationUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User with " + email + " email doesn't exist"));
    }

    public ApplicationUserClient loadUserByClientData(ClientData clientData) {
        return loadAllUserClients().stream()
                .filter(client -> client.getClientData().equals(clientData))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("User with " + clientData + " data doesn't exist"));
    }

    public ApplicationUserDoctor loadUserByDoctorData(DoctorData doctorData) {
        return loadAllUserDoctors().stream()
                .filter(doctor -> doctor.getDoctorData().equals(doctorData))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("User with " + doctorData + " data doesn't exist"));
    }

    public List<ApplicationUserClient> loadAllUserClients() {
        return applicationUserRepository.findAll().stream()
                .filter(user -> user instanceof ApplicationUserClient)
                .map(user -> (ApplicationUserClient) user)
                .collect(Collectors.toList());
    }

    public List<ApplicationUserDoctor> loadAllUserDoctors() {
        return applicationUserRepository.findAll().stream()
                .filter(user -> user instanceof ApplicationUserDoctor)
                .map(user -> (ApplicationUserDoctor) user)
                .collect(Collectors.toList());
    }

    public boolean isUsernameAndEmailAbsent(ApplicationUser rawUser) {
        try {
            checkUsernameAndEmailPresence(rawUser);
            return true;
        } catch (IllegalStateException ex) {
            return false;
        }
    }

    /**
     *
     * @param rawUser applicationUser to check is this rawUser is in the base
     * @return rawUser if this user is not in the base
     * @throws IllegalStateException if this username/email have already been taken
     */
    public ApplicationUser checkUsernameAndEmailPresence(ApplicationUser rawUser) {
        String userUsername = rawUser.getUsername();
        String userEmail = rawUser.getEmail();

        Optional<ApplicationUser> userByEmail =
                applicationUserRepository.findByEmail(userEmail);
        Optional<ApplicationUser> userByUsername =
                applicationUserRepository.findByUsername(userUsername);

        if (userByEmail.isPresent() && userByUsername.isPresent()) {
            throw new IllegalStateException(
                    String.format("Email %s and username %s has already been taken", userEmail, userUsername)
            );
        }
        if (userByUsername.isPresent()) {
            throw new IllegalStateException(
                    String.format("Username %s has already been taken", userUsername)
            );
        }
        if (userByEmail.isPresent()) {
            throw new IllegalStateException(
                    String.format("Email %s has already been taken", userEmail)
            );
        }

        return rawUser;
    }

    public void enableUserByEmail(String email) {
        loadUserByOnlyEmail(email).setEnabled(true);
    }

    public String signUpUser(ApplicationUser rawUser) {
        ApplicationUser user = checkUsernameAndEmailPresence(rawUser);

        String encodedPassword = passwordEncoder
                .encode(user.getPassword());
        user.setPassword(encodedPassword);
        applicationUserRepository.save(user);

        ConfirmationToken confirmationToken =
                TokenHelper.createConfirmationToken(user);

        String token = confirmationToken.getToken();
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    public String resendConfirmationToken(ApplicationUser user) {
        ConfirmationToken unconfirmedToken =
                confirmationTokenService.getAllConfirmationTokens().stream()
                        .filter(confirmationToken ->
                                rawUserAndEncodedUserEquality(user, confirmationToken.getApplicationUser()))
                        .filter(confirmationToken ->
                                confirmationToken.getConfirmedAt() == null)
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("No such user in database"));

        ApplicationUser userFromDB = unconfirmedToken.getApplicationUser();

        ConfirmationToken newConfirmationToken =
                TokenHelper.createConfirmationToken(userFromDB);

        confirmationTokenService.deleteConfirmationToken(unconfirmedToken);
        confirmationTokenService.saveConfirmationToken(newConfirmationToken);

        return newConfirmationToken.getToken();
    }

    private boolean rawUserAndEncodedUserEquality(ApplicationUser rawUser, ApplicationUser encodedUser) {
//        return rawUser.getPersonData().equals(encodedUser.getPersonData()) &&
        return rawUser.getEmail().equals(encodedUser.getEmail()) &&
                rawUser.getUsername().equals(encodedUser.getUsername()) &&
                passwordEncoder.matches(rawUser.getPassword(), encodedUser.getPassword());
    }

}
