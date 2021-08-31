package com.stasroshchenko.clinic.request.visit;

import com.stasroshchenko.clinic.util.VisitStatus;
import com.stasroshchenko.clinic.entity.Visit;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Accept visit - the doctor user has a VisitStatus.SENT visit from a client user
 * and enters the data to accept the visit.
 * SENT -> ACTIVE
 * Made for both encapsulating all doctor user's 'accept' visit data,
 * entered in 'accept' visit form and sending this data as one object
 * to controller. Some fields have validation constraints.
 * @author staffsterr2000
 * @version 1.0
 * @see Visit
 * @see VisitStatus
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AcceptVisitRequest {

    /**
     * Logging
     */
    private final static Logger LOGGER =
            LoggerFactory.getLogger(AcceptVisitRequest.class);



    /**
     * ID of the visit that the doctor user want to accept.
     * Must be not null.
     */
    @NotNull
    private Long visitId;

    /**
     * Non parsed time of the appointment. Must be not blank.
     */
    @NotBlank
    private String appointsAtInput;

    /**
     * Time of the appointment.
     */
    private LocalDateTime appointsAt;



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

}
