package com.stasroshchenko.diploma.entity;

import com.stasroshchenko.diploma.constraint.DateOfBirthConstraint;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
public class PersonData {

    @Id
    @SequenceGenerator(
            name = "person_sequence",
            sequenceName = "person_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "person_sequence"
    )
    private Long id;
    private String firstName;
    private String lastName;

    @DateOfBirthConstraint
    private LocalDate dateOfBirth;

    public PersonData(String firstName, String lastName, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

}
