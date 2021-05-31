package com.stasroshchenko.diploma.service.user;

import com.stasroshchenko.diploma.entity.database.ConfirmationToken;
import com.stasroshchenko.diploma.entity.database.person.ClientData;
import com.stasroshchenko.diploma.entity.database.person.DoctorData;
import com.stasroshchenko.diploma.entity.database.user.ApplicationUser;
import com.stasroshchenko.diploma.entity.database.user.ApplicationUserClient;
import com.stasroshchenko.diploma.entity.database.user.ApplicationUserDoctor;
import com.stasroshchenko.diploma.repository.ApplicationUserRepository;
import com.stasroshchenko.diploma.service.ConfirmationTokenService;
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

    public ApplicationUser loadUserByUser(ApplicationUser rawUser) {
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

    public void enableUserByEmail(String email) {
        loadUserByOnlyEmail(email).setEnabled(true);
    }

    public String signUpUser(ApplicationUser rawUser) {
        ApplicationUser user = loadUserByUser(rawUser);

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

    private void signUpInitialUser(ApplicationUser rawUser) {
        ApplicationUser user = loadUserByUser(rawUser);

        String encodedPassword = passwordEncoder
                .encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setEnabled(true);

        applicationUserRepository.save(user);
    }

    @Bean
    public CommandLineRunner initialUsers() {
        return (args) -> {
            ApplicationUser user1 = new ApplicationUserDoctor(
                    new DoctorData(
                            "Semen",
                            "Lobanov",
                            LocalDate.of(1983, Month.NOVEMBER, 22),
                            29,
                            5
                    ),
                    "lobanov_semen",
                    "lobanov_semen@gmail.com",
                    "password"
            );
            user1.setImageLink("lobanov_semen.jpg");
            signUpInitialUser(user1);

            ApplicationUser user2 = new ApplicationUserClient(
                    new ClientData(
                            "Stanislav",
                            "Roshchenko",
                            LocalDate.of(2000, Month.JULY, 10)
                    ),
                    "roshchenko_stas",
                    "roshchenko_stas@gmail.com",
                    "password"
            );
            signUpInitialUser(user2);

            ApplicationUser user3 = new ApplicationUserDoctor(
                    new DoctorData(
                            "Varvara",
                            "Chernous",
                            LocalDate.of(1988, Month.APRIL, 14),
                            10,
                            3
                    ),
                    "varya69",
                    "varya69@gmail.com",
                    "password"
            );
            user3.setImageLink("chernous_varvara.jpg");
            signUpInitialUser(user3);

            ApplicationUser user4 = new ApplicationUserDoctor(
                    new DoctorData(
                            "Andrey",
                            "Bikov",
                            LocalDate.of(1966, Month.JULY, 22),
                            5,
                            25
                    ),
                    "bikov_v_kedah",
                    "bikov@gmail.com",
                    "password"
            );
            user4.setImageLink("bikov_andrey.jpg");
            signUpInitialUser(user4);

            ApplicationUser user5 = new ApplicationUserDoctor(
                    new DoctorData(
                            "Boris",
                            "Levin",
                            LocalDate.of(1986, Month.JANUARY, 15),
                            5,
                           10
                    ),
                    "levin_v_kedah",
                    "levin_borya@gmail.com",
                    "password"
            );
            user5.setImageLink("levin_boris.jpg");
            signUpInitialUser(user5);

            ApplicationUser user6 = new ApplicationUserDoctor(
                    new DoctorData(
                            "Ivan",
                            "Kupitman",
                            LocalDate.of(1963, Month.MARCH, 13),
                            33,
                           19
                    ),
                    "kupi_dom",
                    "kupitman_vanya@gmail.com",
                    "password"
            );
            user6.setImageLink("kupitman_ivan.jpg");
            signUpInitialUser(user6);

            ApplicationUser user7 = new ApplicationUserDoctor(
                    new DoctorData(
                            "Gleb",
                            "Romanenko",
                            LocalDate.of(1984, Month.SEPTEMBER, 19),
                            14,
                            6
                    ),
                    "gleb_roma",
                    "glebgleb@gmail.com",
                    "password"
            );
            user7.setImageLink("romanenko_gleb.jpg");
            signUpInitialUser(user7);

            ApplicationUser user8 = new ApplicationUserDoctor(
                    new DoctorData(
                            "Phil",
                            "Richards",
                            LocalDate.of(1984, Month.OCTOBER, 5),
                            21,
                            1
                    ),
                    "american_boy",
                    "uedu_s_toboy@gmail.com",
                    "password"
            );
            user8.setImageLink("richards_phil.jpg");
            signUpInitialUser(user8);
        };
    }

}
