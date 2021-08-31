package com.stasroshchenko.clinic.entity.person;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Represents client data.
 * @author staffsterr2000
 * @version 1.0
 * @see PersonData
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@Entity(name = "client_data")
public class ClientData extends PersonData {

    /**
     * Name for the DB sequence
     */
    private static final String SEQUENCE_NAME = "client_data_sequence";



    /**
     * Client's ID
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
     * Creates client data object, with first name, last name, DoB and passport ID.
     * @param firstName client's first name
     * @param lastName client's last name
     * @param dateOfBirth client's DoB
     * @param passportId client's passport ID
     */
    public ClientData(String firstName,
                      String lastName,
                      LocalDate dateOfBirth,
                      Long passportId) {

        super(firstName, lastName, dateOfBirth, passportId);
    }

}
