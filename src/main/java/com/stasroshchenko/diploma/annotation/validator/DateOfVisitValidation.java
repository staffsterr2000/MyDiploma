package com.stasroshchenko.diploma.annotation.validator;

import com.stasroshchenko.diploma.annotation.constraint.DateOfVisitConstraint;
import com.stasroshchenko.diploma.entity.database.Visit;
import com.stasroshchenko.diploma.util.VisitStatus;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

@AllArgsConstructor
public class DateOfVisitValidation implements ConstraintValidator<DateOfVisitConstraint, Visit> {

    @Override
    public void initialize(DateOfVisitConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(Visit visit, ConstraintValidatorContext constraintValidatorContext) {
        if (visit.getStatus() == VisitStatus.SENT) return true;
        LocalDateTime visitDate = visit.getAppointsAt();

//        ZoneId zone = ZoneId.of("Europe/Kiev");

        LocalTime timeWorkdayStarts = LocalTime.of(8, 0);
        LocalTime timeWorkdayEnds = LocalTime.of(18, 0);
        LocalDate today = LocalDate.now();

        return visitDate.isAfter(LocalDateTime.of(today, timeWorkdayStarts)) &&
                visitDate.toLocalTime().isAfter(timeWorkdayStarts) &&           // after any morning AND
                visitDate.toLocalTime().isBefore(timeWorkdayEnds);              // before any evening
    }

}
