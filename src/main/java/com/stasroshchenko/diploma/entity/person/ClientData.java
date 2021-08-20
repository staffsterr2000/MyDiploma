package com.stasroshchenko.diploma.entity.person;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor

@Entity(name = "client_data")
public class ClientData extends PersonData {

    private static final String SEQUENCE_NAME = "client_data_sequence";

    @Id
    @SequenceGenerator(
            name = SEQUENCE_NAME,
            sequenceName = SEQUENCE_NAME,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
//            strategy = GenerationType.IDENTITY
            generator = SEQUENCE_NAME
    )
    @EqualsAndHashCode.Exclude
    private Long id;

    public ClientData(String firstName,
                      String lastName,
                      LocalDate dateOfBirth,
                      Long passportId) {

        super(firstName, lastName, dateOfBirth, passportId);
    }

}
