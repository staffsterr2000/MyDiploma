package com.stasroshchenko.diploma.dataseed;

import com.stasroshchenko.diploma.entity.person.ClientData;
import com.stasroshchenko.diploma.entity.person.DoctorData;
import com.stasroshchenko.diploma.entity.user.ApplicationUser;
import com.stasroshchenko.diploma.entity.user.ApplicationUserClient;
import com.stasroshchenko.diploma.entity.user.ApplicationUserDoctor;
import com.stasroshchenko.diploma.model.repository.ApplicationUserRepository;
import com.stasroshchenko.diploma.model.service.user.ApplicationUserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.util.concurrent.ThreadLocalRandom;

@Component
@AllArgsConstructor
public class ApplicationUserDataLoader implements CommandLineRunner {

    private final static Logger LOGGER =
            LoggerFactory.getLogger(ApplicationUserDataLoader.class);

    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;

    @Override
    public void run(String... args) throws Exception {
        loadApplicationUserDoctorData();
        loadApplicationUserClientData();
    }

    private void loadApplicationUserClientData() {
        // 1. створюється новий клієнт-юзер
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
        // 2. він додається до нашої БД
        signUpInitialUser(user1);
    }

    private void loadApplicationUserDoctorData() {
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
    }

    private void signUpInitialUser(ApplicationUser rawUser) {
        if (applicationUserService.isUsernameAndEmailAbsent(rawUser)) {
            String encodedPassword = passwordEncoder
                    .encode(rawUser.getPassword());
            rawUser.setPassword(encodedPassword);
            rawUser.setEnabled(true);

            applicationUserRepository.save(rawUser);
        }
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

}
