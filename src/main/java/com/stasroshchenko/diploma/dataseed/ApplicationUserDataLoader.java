package com.stasroshchenko.diploma.dataseed;

import com.stasroshchenko.diploma.entity.person.ClientData;
import com.stasroshchenko.diploma.entity.person.DoctorData;
import com.stasroshchenko.diploma.entity.user.ApplicationUser;
import com.stasroshchenko.diploma.entity.user.ApplicationUserClient;
import com.stasroshchenko.diploma.entity.user.ApplicationUserDoctor;
import com.stasroshchenko.diploma.model.repository.ApplicationUserRepository;
import com.stasroshchenko.diploma.model.service.user.ApplicationUserService;
import com.stasroshchenko.diploma.util.DoctorDescriptionHelper;
import com.stasroshchenko.diploma.util.PassportIdGenerator;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;

/**
 * Loads users to db within start of the program
 * @author staffsterr2000
 * @version 1.0
 * @see CommandLineRunner
 */
@Component
@AllArgsConstructor
public class ApplicationUserDataLoader implements CommandLineRunner {

    /**
     * Autowired repository for working with DB users
     */
    private final ApplicationUserRepository applicationUserRepository;

    /**
     * Autowired service for obtaining and processing users
     */
    private final ApplicationUserService applicationUserService;

    /**
     * Autowired password encoder for encoding and decoding user passwords
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Runs after automatic creation of the {@link ApplicationUserDataLoader}
     * @param args arguments that are passed into the method
     * @throws Exception exception that may occur by using this method
     * @since 1.0
     * @see CommandLineRunner
     * @see Component
     */
    @Override
    public void run(String... args) throws Exception {
        loadApplicationUserDoctorData();
        loadApplicationUserClientData();
    }

    /**
     * Loads all the initial client users
     * @since 1.0
     * @see PassportIdGenerator
     */
    private void loadApplicationUserClientData() {
        ApplicationUserClient user1 = new ApplicationUserClient(
                new ClientData(
                        "Stanislav",
                        "Roshchenko",
                        LocalDate.of(2000, Month.JULY, 10),
                        PassportIdGenerator.generatePassportId()
                ),
                "roshchenko_stas",
                "roshchenko_stas@gmail.com",
                "password"
        );
        signUpInitialUser(user1);
    }

    /**
     * Loads all the initial doctor users
     * @since 1.0
     * @see PassportIdGenerator
     * @see DoctorDescriptionHelper
     */
    private void loadApplicationUserDoctorData() {
        ApplicationUserDoctor user1 = new ApplicationUserDoctor(
                new DoctorData(
                        "Semen",
                        "Lobanov",
                        LocalDate.of(1983, Month.NOVEMBER, 22),
                        PassportIdGenerator.generatePassportId(),
                        29,
                        5
                ),
                "lobanov_semen",
                "lobanov_semen@gmail.com",
                "password"
        );
        DoctorData doctor1 = user1.getDoctorData();
        doctor1.setDescription(DoctorDescriptionHelper.getDescriptionForDoctor(doctor1));
        user1.setImageLink("lobanov_semen.jpg");
        signUpInitialUser(user1);

        ApplicationUserDoctor user2 = new ApplicationUserDoctor(
                new DoctorData(
                        "Varvara",
                        "Chernous",
                        LocalDate.of(1988, Month.APRIL, 14),
                        PassportIdGenerator.generatePassportId(),
                        10,
                        3
                ),
                "varya69",
                "varya69@gmail.com",
                "password"
        );
        DoctorData doctor2 = user2.getDoctorData();
        doctor2.setDescription(DoctorDescriptionHelper.getDescriptionForDoctor(doctor2));
        user2.setImageLink("chernous_varvara.jpg");
        signUpInitialUser(user2);


        ApplicationUserDoctor user3 = new ApplicationUserDoctor(
                new DoctorData(
                        "Phil",
                        "Richards",
                        LocalDate.of(1984, Month.OCTOBER, 5),
                        PassportIdGenerator.generatePassportId(),
                        21,
                        1
                ),
                "american_boy",
                "uedu_s_toboy@gmail.com",
                "password"
        );
        DoctorData doctor3 = user3.getDoctorData();
        doctor3.setDescription(DoctorDescriptionHelper.getDescriptionForDoctor(doctor3));
        user3.setImageLink("richards_phil.jpg");
        signUpInitialUser(user3);

        ApplicationUserDoctor user4 = new ApplicationUserDoctor(
                new DoctorData(
                        "Andrey",
                        "Bikov",
                        LocalDate.of(1966, Month.JULY, 22),
                        PassportIdGenerator.generatePassportId(),
                        5,
                        25
                ),
                "bikov_v_kedah",
                "bikov@gmail.com",
                "password"
        );
        DoctorData doctor4 = user4.getDoctorData();
        doctor4.setDescription(DoctorDescriptionHelper.getDescriptionForDoctor(doctor4));
        user4.setImageLink("bikov_andrey.jpg");
        signUpInitialUser(user4);

        ApplicationUserDoctor user5 = new ApplicationUserDoctor(
                new DoctorData(
                        "Boris",
                        "Levin",
                        LocalDate.of(1986, Month.JANUARY, 15),
                        PassportIdGenerator.generatePassportId(),
                        5,
                        10
                ),
                "levin_v_kedah",
                "levin_borya@gmail.com",
                "password"
        );
        DoctorData doctor5 = user5.getDoctorData();
        doctor5.setDescription(DoctorDescriptionHelper.getDescriptionForDoctor(doctor5));
        user5.setImageLink("levin_boris.jpg");
        signUpInitialUser(user5);

        ApplicationUserDoctor user6 = new ApplicationUserDoctor(
                new DoctorData(
                        "Ivan",
                        "Kupitman",
                        LocalDate.of(1963, Month.MARCH, 13),
                        PassportIdGenerator.generatePassportId(),
                        33,
                        19
                ),
                "kupi_dom",
                "kupitman_vanya@gmail.com",
                "password"
        );
        DoctorData doctor6 = user6.getDoctorData();
        doctor6.setDescription(DoctorDescriptionHelper.getDescriptionForDoctor(doctor6));
        user6.setImageLink("kupitman_ivan.jpg");
        signUpInitialUser(user6);

        ApplicationUserDoctor user7 = new ApplicationUserDoctor(
                new DoctorData(
                        "Gleb",
                        "Romanenko",
                        LocalDate.of(1984, Month.SEPTEMBER, 19),
                        PassportIdGenerator.generatePassportId(),
                        14,
                        6
                ),
                "gleb_roma",
                "glebgleb@gmail.com",
                "password"
        );
        DoctorData doctor7 = user7.getDoctorData();
        doctor7.setDescription(DoctorDescriptionHelper.getDescriptionForDoctor(doctor7));
        user7.setImageLink("romanenko_gleb.jpg");
        signUpInitialUser(user7);
    }

    /**
     * Checks for absence the user in database, encodes rawUser's password and saves the user to memory
     * @param rawUser decoded user, which will be checked for absence
     *                and if it's absent, it will be encoded and saved.
     * @since 1.0
     */
    private void signUpInitialUser(ApplicationUser rawUser) {
        if (applicationUserService.isUsernameAndEmailAbsent(rawUser)) {
            String encodedPassword = passwordEncoder
                    .encode(rawUser.getPassword());
            rawUser.setPassword(encodedPassword);
            rawUser.setEnabled(true);

            applicationUserRepository.save(rawUser);
        }
    }

}
