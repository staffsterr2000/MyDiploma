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
@DiscriminatorValue(value = "doctor")
public class DoctorData extends PersonData {

    public DoctorData(String firstName, String lastName, LocalDate dateOfBirth) {
        super(firstName, lastName, dateOfBirth);
    }

}
