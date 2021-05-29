package com.stasroshchenko.diploma.entity.database.person;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor

@Entity(name = "doctor_data")
public class DoctorData extends PersonData {

    private static final String SEQUENCE_NAME = "doctor_data_sequence";

    @Id
    @SequenceGenerator(
            name = SEQUENCE_NAME,
            sequenceName = SEQUENCE_NAME,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = SEQUENCE_NAME
    )
    @EqualsAndHashCode.Exclude
    private Long id;
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
