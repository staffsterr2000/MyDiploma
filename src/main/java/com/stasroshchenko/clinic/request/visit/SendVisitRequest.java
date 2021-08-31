package com.stasroshchenko.clinic.request.visit;

import com.stasroshchenko.clinic.util.VisitStatus;
import com.stasroshchenko.clinic.entity.Visit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Send visit - the client user sends VisitStatus.SENT visit to doctor.
 * -> SENT
 * Made for both encapsulating all client user's 'send' visit data,
 * entered in 'send' visit form adn sending this data as one object
 * to controller. All fields have validation constraints.
 * @author staffsterr2000
 * @version 1.0
 * @see Visit
 * @see VisitStatus
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SendVisitRequest {

    /**
     * ID of the doctor, with whom the client want to have
     * an appointment. Must be not null.
     */
    @NotNull
    private Long doctorDataId;

    /**
     * Client's complaint on his health. Must be non empty.
     */
    @NotBlank
    private String complaint;

}
