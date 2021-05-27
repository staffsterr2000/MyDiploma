package com.stasroshchenko.diploma.entity.database.person;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor

@Entity(name = "client_data")
@DiscriminatorValue(value = "CLIENT")
public class ClientData extends PersonData {

    private String complaint;

    public ClientData(String firstName, String lastName, LocalDate dateOfBirth) {
        super(firstName, lastName, dateOfBirth);
    }

    public void addComplaint(String complaint) {
        this.complaint = complaint;
    }

}
