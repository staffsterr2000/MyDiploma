package com.stasroshchenko.diploma.request;

import com.stasroshchenko.diploma.util.annotation.constraint.*;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@AllArgsConstructor
@NoArgsConstructor
@Data

@PasswordMatchConstraint        // паролі мають бути однакові
public class RegistrationRequest {

    private final static Logger LOGGER =
            LoggerFactory.getLogger(RegistrationRequest.class);

    @NotBlank       // поле має бути не пустим
    private String firstName;

    @NotBlank       // поле має бути не пустим
    private String lastName;

    @NotBlank       // поле має бути не пустим
    private String dateOfBirthInput;

    @DateOfBirthConstraint      // дата народження має бути між 01.01.1900 та (сьогоднішний день - 18 років)
    private LocalDate dateOfBirth;

    @Size(min = 3, max = 24, message = "Username must have from 3 to 24 symbols")   // розмір вводу
    @UniqueUsernameConstraint   // перевірка унікальності username
    @UsernameConstraint         // перевірка правильності username [A-Za-z0-9_]+
    private String username;

    @UniqueEmailConstraint      // перевірка унікальності email
    @EmailConstraint            // перевірка правильності email
    // ^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+)*
    //                            @[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)+$
    private String email;

    @UniqueClientPassportIdConstraint   // перевірка унікальності паспорт ID
    private Long passportId;

    @Size(min = 8, max = 24, message = "Password must have from 8 to 24 symbols")   // розмір вводу
    private String password;
    @Size(min = 8, max = 24, message = "Password must have from 8 to 24 symbols")   // розмір вводу
    private String repeatedPassword;

    public void setDateOfBirthInput(String dateOfBirthInput) {
        this.dateOfBirthInput = dateOfBirthInput;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            this.dateOfBirth = LocalDate.parse(dateOfBirthInput, formatter);

        } catch (DateTimeParseException ex) {

            LOGGER.error(ex.getMessage());
            this.dateOfBirth = LocalDate.MIN;

        }
    }

}
