package com.stasroshchenko.diploma.entity.database.person;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor

@Entity(name = "doctor_data")
@DiscriminatorValue(value = "DOCTOR")
public class DoctorData extends PersonData {

    private Integer room;
    private Integer experienceAge;

    public DoctorData(String firstName,
                      String lastName,
                      LocalDate dateOfBirth,
                      Integer room,
                      Integer experienceAge) {

        super(firstName, lastName, dateOfBirth);
        this.room = room;
        this.experienceAge = experienceAge;
    }

}
