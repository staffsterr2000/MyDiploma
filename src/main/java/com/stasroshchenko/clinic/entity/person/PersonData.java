package com.stasroshchenko.clinic.entity.person;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Represents data about abstract person separately to the user's data.
 * @author staffsterr2000
 * @version 1.0
 */
@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class PersonData {

    /**
     * First name of the person
     */
    @Column
    protected String firstName;

    /**
     * Last name of the person
     */
    @Column
    protected String lastName;

    /**
     * Person's date of birth
     */
    @Column
    protected LocalDate dateOfBirth;

    /**
     * Person's passport ID
     */
    @Column(unique = true)
    protected Long passportId;



    /**
     * Super constructor, uses first name, last name, DoB and passport ID.
     * @param firstName person's first name
     * @param lastName person's last name
     * @param dateOfBirth person's date of birth
     * @param passportId person's passport ID
     */
    public PersonData(String firstName, String lastName, LocalDate dateOfBirth, Long passportId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.passportId = passportId;
    }

}
