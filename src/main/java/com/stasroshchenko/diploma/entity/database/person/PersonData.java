package com.stasroshchenko.diploma.entity.database.person;

import com.stasroshchenko.diploma.entity.database.Visit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="person_role")
public abstract class PersonData {

    private static final String SEQUENCE_NAME = "person_data_sequence";

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
    protected String firstName;
    protected String lastName;
    protected LocalDate dateOfBirth;
//    protected List<Visit> visits;

    public PersonData(String firstName, String lastName, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
//        this.visits = new ArrayList<>();
    }

}
