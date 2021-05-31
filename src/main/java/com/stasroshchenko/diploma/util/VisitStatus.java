package com.stasroshchenko.diploma.util;

public enum VisitStatus {
    SENT("Your visit request will be read by our specialist as soon as possible"),
    ACTIVE("Your visit request was read and accepted by the doctor. We're waiting for you soon!"),
    CANCELLED("Your visit was suddenly denied try again or phone us"),
    OCCURRED("The visit occurred. Thank you for coming!"),
    NOT_OCCURRED("For some reason the visit didn't occur.");

    VisitStatus(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    private final String statusMessage;

    public String getStatusMessage() {
        return statusMessage;
    }
}
