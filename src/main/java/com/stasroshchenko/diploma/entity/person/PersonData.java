package com.stasroshchenko.diploma.entity.person;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Represents data about person separately to the user's data
 * @author staffsterr2000
 * @version 1.0
 */
@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class PersonData {

    @Column
    protected String firstName;

    @Column
    protected String lastName;

    @Column
    protected LocalDate dateOfBirth;

    @Column(unique = true)
    protected Long passportId;

    public PersonData(String firstName, String lastName, LocalDate dateOfBirth, Long passportId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.passportId = passportId;
    }

}
