package com.stasroshchenko.diploma.util;

public enum VisitStatus {
    ACTIVE(1),
    SENT(2),
    OCCURRED(3),
    NOT_OCCURRED(4),
    CANCELLED_BY_DOCTOR(5),
    CANCELLED_BY_CLIENT(6);

    private final Integer value;

    VisitStatus(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
