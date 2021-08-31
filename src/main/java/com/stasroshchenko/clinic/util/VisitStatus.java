package com.stasroshchenko.clinic.util;

import com.stasroshchenko.clinic.entity.Visit;

/**
 * Represents possible visit statuses.
 * @author staffsterr2000
 * @version 1.0
 * @see Visit
 */
public enum VisitStatus {

    /**
     * The visit is confirmed by the doctor, and the appointment will occur soon.
     * Level of importance while sorting is 1.
     */
    ACTIVE(1),

    /**
     * The visit was sent by client to the doctor, and the client waits for the response:
     * confirm or decline answer made by doctor
     * Level of importance while sorting is 2.
     */
    SENT(2),

    /**
     * The visit time passed and the client was at the clinic during the visit
     * Level of importance while sorting is 3.
     */
    OCCURRED(3),

    /**
     * The visit time passed and the client didn't come at the clinic at the time
     * Level of importance while sorting is 4.
     */
    NOT_OCCURRED(4),

    /**
     * The doctor denied the visit for some reason
     * Level of importance is 5.
     */
    CANCELLED_BY_DOCTOR(5),

    /**
     * The client cancelled the visit for some reason
     * Level of importance while sorting is 6.
     */
    CANCELLED_BY_CLIENT(6);



    /**
     * Importance value of the status. Used for sorting visits by their visit statuses' values
     */
    private final Integer value;



    /**
     * Creates {@link VisitStatus} instance
     * @param value visit status's importance value
     * @since 1.0
     */
    VisitStatus(Integer value) {
        this.value = value;
    }



    /**
     * Gets importance value
     * @return visit status's value
     * @since 1.0
     */
    public Integer getValue() {
        return value;
    }
}
