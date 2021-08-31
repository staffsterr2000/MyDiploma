package com.stasroshchenko.clinic.request.visit;

import com.stasroshchenko.clinic.entity.Visit;
import com.stasroshchenko.clinic.util.VisitStatus;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Decline visit - the doctor user has a VisitStatus.SENT visit from
 * a client user and chooses the visit to decline
 * SENT -> CANCELLED_BY_DOCTOR
 * Made for both encapsulating all doctor user's 'decline' visit data,
 * entered in 'decline' visit form and sending this data as one object
 * to controller. All fields have validation constraints.
 * @author staffsterr2000
 * @version 1.0
 * @see Visit
 * @see VisitStatus
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeclineVisitRequest {

    /**
     * ID of the visit that the doctor user want to decline.
     * Must be not null.
     */
    @NotNull
    private Long visitId;

}
