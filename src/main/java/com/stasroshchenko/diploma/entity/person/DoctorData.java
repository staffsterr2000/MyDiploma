package com.stasroshchenko.diploma.entity.person;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Represents doctor data.
 * @author staffsterr2000
 * @version 1.0
 * @see PersonData
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@Entity(name = "doctor_data")
public class DoctorData extends PersonData {

    /**
     * Name for the DB sequence
     */
    private static final String SEQUENCE_NAME = "doctor_data_sequence";



    /**
     * Doctor's ID
     */
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

    /**
     * Doctor's visiting room
     */
    @Column
    private Integer room;

    /**
     * Quantity of years that the doctor has spent working as a doctor
     */
    @Column
    private Integer experienceAge;

    /**
     * Doctor's biography
     */
    @EqualsAndHashCode.Exclude
    @Column(length = 1024)
    private String description;



    /**
     * Creates doctor data object, with first name, last name, DoB, passport ID, visiting room and experience age.
     * @param firstName doctor's first name
     * @param lastName doctor's last name
     * @param dateOfBirth doctor's DoB
     * @param passportId doctor's passport ID
     * @param room doctor's visiting room
     * @param experienceAge doctor's experience (in years)
     */
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
