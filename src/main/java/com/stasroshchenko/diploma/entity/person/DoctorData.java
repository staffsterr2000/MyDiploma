package com.stasroshchenko.diploma.entity.person;

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
//            strategy = GenerationType.IDENTITY,
            generator = SEQUENCE_NAME
    )
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column
    private Integer room;

    @Column
    private Integer experienceAge;

    @EqualsAndHashCode.Exclude
    @Column(length = 1024)
    private String description;

    public DoctorData(String firstName,
                      String lastName,
                      LocalDate dateOfBirth,
                      Long passportId,
                      Integer room,
                      Integer experienceAge) {

        super(firstName, lastName, dateOfBirth, passportId);
        this.room = room;
        this.experienceAge = experienceAge;
    }

}
