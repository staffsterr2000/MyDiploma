package com.stasroshchenko.diploma.request.visit;

import com.stasroshchenko.diploma.entity.Visit;
import com.stasroshchenko.diploma.util.VisitStatus;
import com.stasroshchenko.diploma.util.annotation.constraint.DateOfBirthConstraint;
import com.stasroshchenko.diploma.util.annotation.constraint.UniqueClientPassportIdConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Create visit - the doctor user has a data about non registered user,
 * that needs in an appointment with this specialist and creates
 * VisitStatus.ACTIVE visit.
 * -> ACTIVE
 * Made for both encapsulating all doctor user's 'create' visit data,
 * entered in 'create' visit form and sending this data as one object
 * to controller. Some fields have validation constraints.
 * @author staffsterr2000
 * @version 1.0
 * @see Visit
 * @see VisitStatus
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateVisitRequest {

    /**
     * Logging
     */
    private final static Logger LOGGER =
            LoggerFactory.getLogger(CreateVisitRequest.class);



    /**
     * First name of the client. Must be not blank.
     */
    @NotBlank
    private String clientFirstName;

    /**
     * Last name of the client. Must be not blank.
     */
    @NotBlank
    private String clientLastName;

    /**
     * Client's passport ID. Must be unique.
     */
    // new
    @UniqueClientPassportIdConstraint
    private Long clientPassportId;

    /**
     * Client's DoB. Must be correct.
     */
    @DateOfBirthConstraint
    private LocalDate clientDateOfBirth;

    /**
     * Client's non parsed DoB. Must be not blank.
     */
    @NotBlank
    private String clientDateOfBirthInput;

    /**
     * Time of the appointment
     */
    private LocalDateTime appointsAt;

    /**
     * Non parsed time of the appointment. Must be not blank.
     */
    @NotBlank
    private String appointsAtInput;

    /**
     * Client's complaint on his health.
     */
    @NotBlank
    private String clientComplaint;



    /**
     * Tries to parse appointment's string time to date time and set
     * these fields.
     * @param appointsAtInput string to parse
     * @since 1.0
     */
    public void setAppointsAtInput(String appointsAtInput) {
        this.appointsAtInput = appointsAtInput;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        try {
            this.appointsAt = LocalDateTime.parse(appointsAtInput, formatter);

        } catch (DateTimeParseException ex) {
            appointsAt = LocalDateTime.MIN;

        }

    }

    /**
     * Tries to parse string DoB to date DoB and set these fields.
     * @param clientDateOfBirthInput string to parse
     * @since 1.0
     */
    public void setClientDateOfBirthInput(String clientDateOfBirthInput) {
        this.clientDateOfBirthInput = clientDateOfBirthInput;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            this.clientDateOfBirth = LocalDate.parse(clientDateOfBirthInput, formatter);

        } catch (DateTimeParseException ex) {

            LOGGER.error(ex.getMessage());
            this.clientDateOfBirth = LocalDate.MIN;

        }
    }

}
