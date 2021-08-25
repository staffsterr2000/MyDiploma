package com.stasroshchenko.diploma.request.visit;

import com.stasroshchenko.diploma.entity.Visit;
import com.stasroshchenko.diploma.util.VisitStatus;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pass visit - the doctor user has a VisitStatus.ACTIVE visit and
 * wants to modify the visit's status.
 * ACTIVE -> OCCURRED/NOT_OCCURRED/CANCELLED_BY_DOCTOR.
 * Made for both encapsulating all doctor user's 'pass' visit data,
 * entered in 'pass' visit form and sending this data as one object
 * to controller. Some fields have validation constraints.
 * @author staffsterr2000
 * @version 1.0
 * @see Visit
 * @see VisitStatus
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PassVisitRequest {

    /**
     * ID of the visit which the doctor user want to change status.
     * Must be not null.
     */
    @NotNull
    private Long visitId;

    /**
     * New status for the visit. Can be OCCURRED, NOT_OCCURRED and
     * CANCELLED_BY_DOCTOR.
     */
    private VisitStatus status;

}
