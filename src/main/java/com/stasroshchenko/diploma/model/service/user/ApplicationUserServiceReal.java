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
import java.util.concurrent.ThreadLocalRandom;
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

    private String getDescriptionForDoctor(DoctorData doctorData) {
        String lastName = doctorData.getLastName();
        Integer experience = doctorData.getExperienceAge();

        return String.format("Dr. %1$s joined the DeDentist? Clinic staff in %2$d. He received his medical degree from" +
                "the University of Oklahoma College of Medicine.%nHe then completed his Internal Medicine training" +
                "at the University of Oklahoma in Oklahoma City.%nDr. %1$s went on to complete his fellowship training" +
                "in Allergy and Immunology at University of Texas Medical Branch in Galveston.%n" +
                "Dr. %1$s enjoys spending time with his family and resides in Edmond, Oklahoma.%nHis hobbies include exercise, " +
                "golfing and snow skiing.", lastName, LocalDate.now().getYear() - experience);
    }

    private Long generatePassportId() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextLong(1_000_000_000L, 10_000_000_000L);
    }

    @Bean
    public CommandLineRunner initiateClients() {
        return (args) -> {
            ApplicationUserClient user1 = new ApplicationUserClient(
                    new ClientData(
                            "Stanislav",
                            "Roshchenko",
                            LocalDate.of(2000, Month.JULY, 10),
                            generatePassportId()
                    ),
                    "roshchenko_stas",
                    "roshchenko_stas@gmail.com",
                    "password"
            );
            signUpInitialUser(user1);
        };
    }

    @Bean
    public CommandLineRunner initiateDoctors() {
        return (args) -> {
            ApplicationUserDoctor user1 = new ApplicationUserDoctor(
                    new DoctorData(
                            "Semen",
                            "Lobanov",
                            LocalDate.of(1983, Month.NOVEMBER, 22),
                            generatePassportId(),
                            29,
                            5
                    ),
                    "lobanov_semen",
                    "lobanov_semen@gmail.com",
                    "password"
            );
            DoctorData doctor1 = user1.getDoctorData();
            doctor1.setDescription(getDescriptionForDoctor(doctor1));
            user1.setImageLink("lobanov_semen.jpg");
            signUpInitialUser(user1);

            ApplicationUserDoctor user2 = new ApplicationUserDoctor(
                    new DoctorData(
                            "Varvara",
                            "Chernous",
                            LocalDate.of(1988, Month.APRIL, 14),
                            generatePassportId(),
                            10,
                            3
                    ),
                    "varya69",
                    "varya69@gmail.com",
                    "password"
            );
            DoctorData doctor2 = user2.getDoctorData();
            doctor2.setDescription(getDescriptionForDoctor(doctor2));
            user2.setImageLink("chernous_varvara.jpg");
            signUpInitialUser(user2);

            ApplicationUserDoctor user3 = new ApplicationUserDoctor(
                    new DoctorData(
                            "Phil",
                            "Richards",
                            LocalDate.of(1984, Month.OCTOBER, 5),
                            generatePassportId(),
                            21,
                            1
                    ),
                    "american_boy",
                    "uedu_s_toboy@gmail.com",
                    "password"
            );
            DoctorData doctor3 = user3.getDoctorData();
            doctor3.setDescription(getDescriptionForDoctor(doctor3));
            user3.setImageLink("richards_phil.jpg");
            signUpInitialUser(user3);

            ApplicationUserDoctor user4 = new ApplicationUserDoctor(
                    new DoctorData(
                            "Andrey",
                            "Bikov",
                            LocalDate.of(1966, Month.JULY, 22),
                            generatePassportId(),
                            5,
                            25
                    ),
                    "bikov_v_kedah",
                    "bikov@gmail.com",
                    "password"
            );
            DoctorData doctor4 = user4.getDoctorData();
            doctor4.setDescription(getDescriptionForDoctor(doctor4));
            user4.setImageLink("bikov_andrey.jpg");
            signUpInitialUser(user4);

            ApplicationUserDoctor user5 = new ApplicationUserDoctor(
                    new DoctorData(
                            "Boris",
                            "Levin",
                            LocalDate.of(1986, Month.JANUARY, 15),
                            generatePassportId(),
                            5,
                           10
                    ),
                    "levin_v_kedah",
                    "levin_borya@gmail.com",
                    "password"
            );
            DoctorData doctor5 = user5.getDoctorData();
            doctor5.setDescription(getDescriptionForDoctor(doctor5));
            user5.setImageLink("levin_boris.jpg");
            signUpInitialUser(user5);

            ApplicationUserDoctor user6 = new ApplicationUserDoctor(
                    new DoctorData(
                            "Ivan",
                            "Kupitman",
                            LocalDate.of(1963, Month.MARCH, 13),
                            generatePassportId(),
                            33,
                           19
                    ),
                    "kupi_dom",
                    "kupitman_vanya@gmail.com",
                    "password"
            );
            DoctorData doctor6 = user6.getDoctorData();
            doctor6.setDescription(getDescriptionForDoctor(doctor6));
            user6.setImageLink("kupitman_ivan.jpg");
            signUpInitialUser(user6);

            ApplicationUserDoctor user7 = new ApplicationUserDoctor(
                    new DoctorData(
                            "Gleb",
                            "Romanenko",
                            LocalDate.of(1984, Month.SEPTEMBER, 19),
                            generatePassportId(),
                            14,
                            6
                    ),
                    "gleb_roma",
                    "glebgleb@gmail.com",
                    "password"
            );
            DoctorData doctor7 = user7.getDoctorData();
            doctor7.setDescription(getDescriptionForDoctor(doctor7));
            user7.setImageLink("romanenko_gleb.jpg");
            signUpInitialUser(user7);
        };
    }

}
