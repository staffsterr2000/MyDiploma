package com.stasroshchenko.diploma.entity.request.visit;

import com.stasroshchenko.diploma.annotation.constraint.DateOfBirthConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateVisitRequest {

    private final static Logger LOGGER =
            LoggerFactory.getLogger(CreateVisitRequest.class);

    @NotBlank
    private String clientFirstName;

    @NotBlank
    private String clientLastName;

    @DateOfBirthConstraint
    private LocalDate clientDateOfBirth;

    @NotBlank
    private String clientDateOfBirthInput;

    private LocalDateTime appointsAt;

    @NotBlank
    private String appointsAtInput;

    @NotBlank
    private String clientComplaint;

    public void setAppointsAtInput(String appointsAtInput) {
        this.appointsAtInput = appointsAtInput;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        try {
            this.appointsAt = LocalDateTime.parse(appointsAtInput, formatter);

        } catch (DateTimeParseException ex) {
            appointsAt = LocalDateTime.MIN;

        }

    }

    public void setClientDateOfBirthInput(String clientDateOfBirthInput) {
        this.clientDateOfBirthInput = clientDateOfBirthInput;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            this.clientDateOfBirth = LocalDate.parse(clientDateOfBirthInput, formatter);

        } catch (DateTimeParseException ex) {

            LOGGER.error(ex.getMessage());
            this.clientDateOfBirth = LocalDate.MIN;

        }
    }
}
