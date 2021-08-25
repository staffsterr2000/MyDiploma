package com.stasroshchenko.diploma.request;

import com.stasroshchenko.diploma.util.annotation.constraint.*;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Made for both encapsulating all client user's registration data, entered
 * in Registration page form and sending this data as one object to
 * controller. Has fields with validation constraints.
 * @author staffsterr2000
 * @version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@PasswordMatchConstraint
public class RegistrationRequest {

    /**
     * Logging
     */
    private final static Logger LOGGER =
            LoggerFactory.getLogger(RegistrationRequest.class);



    /**
     * Client's first name. Must be not empty.
     */
    @NotBlank
    private String firstName;

    /**
     * Client's last name. Must be not empty.
     */
    @NotBlank
    private String lastName;

    /**
     * Client's non parsed DoB. Must be not empty.
     */
    @NotBlank
    private String dateOfBirthInput;

    /**
     * Client's DoB. Must be correct.
     */
    @DateOfBirthConstraint
    private LocalDate dateOfBirth;

    /**
     * Client user's username. Must be correct and unique.
     */
    @Size(min = 3, max = 24, message = "Username must have from 3 to 24 symbols")
    @UniqueUsernameConstraint
    @UsernameConstraint
    private String username;

    /**
     * Client user's email. Must be correct and unique.
     */
    @UniqueEmailConstraint
    @EmailConstraint
    private String email;

    /**
     * Client's passport ID. Must be unique.
     */
    @UniqueClientPassportIdConstraint
    private Long passportId;

    /**
     * Client user's password. Must have correct length.
     */
    @Size(min = 8, max = 24, message = "Password must have from 8 to 24 symbols")
    private String password;

    /**
     * Client user's repeated password. Must match first one.
     */
    @Size(min = 8, max = 24, message = "Password must have from 8 to 24 symbols")
    private String repeatedPassword;



    /**
     * Tries to parse string DoB to date DoB and set these fields.
     * @param dateOfBirthInput string to parse
     * @since 1.0
     */
    public void setDateOfBirthInput(String dateOfBirthInput) {
        this.dateOfBirthInput = dateOfBirthInput;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            this.dateOfBirth = LocalDate
                    .parse(dateOfBirthInput, formatter);
        } catch (DateTimeParseException ex) {
            // if exception is caught, it will be logged and date will be set to MIN,
            // and following DateOfBirthConstraint will throw exception about the date
            // of birth validness
            LOGGER.error(ex.getMessage());
            this.dateOfBirth = LocalDate.MIN;
        }
    }

}
