package com.stasroshchenko.clinic.request.visit;

import com.stasroshchenko.clinic.entity.Visit;
import com.stasroshchenko.clinic.util.VisitStatus;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Cancel visit - the client user want to cancel VisitStatus.SENT or
 * already VisitStatus.ACTIVE visit.
 * SENT/ACTIVE -> CANCELLED_BY_CLIENT
 * Made for both encapsulating all client user's 'cancel' visit data,
 * entered in 'cancel' visit form and sending this data as one object
 * to controller. All fields have validation constraints.
 * @author staffsterr2000
 * @version 1.0
 * @see Visit
 * @see VisitStatus
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelVisitRequest {

    /**
     * ID of the visit that the client user want to cancel.
     * Must be not null.
     */
    @NotNull
    private Long visitId;

}
