package com.stasroshchenko.diploma.service;

import com.stasroshchenko.diploma.entity.database.ApplicationUser;
import com.stasroshchenko.diploma.entity.database.person.ClientData;
import com.stasroshchenko.diploma.entity.database.person.DoctorData;
import com.stasroshchenko.diploma.repository.ApplicationUserRepository;
import com.stasroshchenko.diploma.entity.database.ConfirmationToken;
import com.stasroshchenko.diploma.util.TokenHelper;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ApplicationUserService implements UserDetailsService {

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

    public void enableUserByEmail(String email) {
        loadUserByOnlyEmail(email).setEnabled(true);
    }


    public String signUpUser(ApplicationUser rawUser) {
        String userUsername = rawUser.getUsername();
        String userEmail = rawUser.getEmail();

        Optional<ApplicationUser> userByEmail =
                applicationUserRepository.findByEmail(userEmail);
        Optional<ApplicationUser> userByUsername =
                applicationUserRepository.findByUsername(userUsername);

        if (userByEmail.isPresent() && userByUsername.isPresent()) {
            ConfirmationToken unconfirmedToken =
                    confirmationTokenService.getAllConfirmationTokens().stream()
                            .filter(confirmationToken ->
                                    rawUserAndEncodedUserEquality(rawUser, confirmationToken.getApplicationUser()))
                            .filter(confirmationToken ->
                                    confirmationToken.getConfirmedAt() == null)
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException(
                                    String.format("Email %s and username %s has already been taken", userEmail, userUsername)
                            ));

            ApplicationUser userFromDB = unconfirmedToken.getApplicationUser();

            ConfirmationToken newConfirmationToken =
                    TokenHelper.createConfirmationToken(userFromDB);
            String token = newConfirmationToken.getToken();

            confirmationTokenService.deleteConfirmationToken(unconfirmedToken);
            confirmationTokenService.saveConfirmationToken(newConfirmationToken);

            return token;

        } else if (userByUsername.isPresent()) {
            throw new IllegalStateException(
                    String.format("Username %s has already been taken", userUsername)
            );
        } else if (userByEmail.isPresent()) {
            throw new IllegalStateException(
                    String.format("Email %s has already been taken", userEmail)
            );
        }

        String encodedPassword = passwordEncoder
                .encode(rawUser.getPassword());
        rawUser.setPassword(encodedPassword);
        applicationUserRepository.save(rawUser);

        ConfirmationToken confirmationToken =
                TokenHelper.createConfirmationToken(rawUser);

        String token = confirmationToken.getToken();
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    public boolean rawUserAndEncodedUserEquality(ApplicationUser rawUser, ApplicationUser encodedUser) {
        return rawUser.getPersonData().equals(encodedUser.getPersonData()) &&
                rawUser.getEmail().equals(encodedUser.getEmail()) &&
                rawUser.getUsername().equals(encodedUser.getUsername()) &&
                passwordEncoder.matches(rawUser.getPassword(), encodedUser.getPassword());
    }

    @Bean
    public CommandLineRunner initialUsers() {
        return (args) -> {
            ApplicationUser user1 = new ApplicationUser(
                    new DoctorData(
                            "Semen",
                            "Lobanov",
                            LocalDate.of(1983, Month.NOVEMBER, 22)
                    ),
                    "lobanov_semen",
                    "lobanov_semen@gmail.com",
                    "password"
            );

            String encodedPassword1 = passwordEncoder
                    .encode(user1.getPassword());
            user1.setPassword(encodedPassword1);
            user1.setEnabled(true);
            applicationUserRepository.save(user1);

            ApplicationUser user2 = new ApplicationUser(
                    new ClientData(
                            "Stanislav",
                            "Roshchenko",
                            LocalDate.of(2000, Month.JULY, 10)
                    ),
                    "roshchenko_stas",
                    "roshchenko_stas@gmail.com",
                    "password"
            );

//            ClientData clientData = (ClientData) user2.getPersonData();
//            clientData.addComplaint("Tooth broke");

            String encodedPassword2 = passwordEncoder
                    .encode(user2.getPassword());
            user2.setPassword(encodedPassword2);
            user2.setEnabled(true);
            applicationUserRepository.save(user2);
        };
    }

}
