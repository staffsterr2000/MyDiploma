package com.stasroshchenko.diploma.entity;

import com.stasroshchenko.diploma.entity.person.ClientData;
import com.stasroshchenko.diploma.entity.person.DoctorData;
import com.stasroshchenko.diploma.util.VisitStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents visit entity, is used for tracking visits between a client and a doctor
 * @author staffsterr2000
 * @version 1.0
 * @see DoctorData
 * @see ClientData
 * @see VisitStatus
 */
@Data
@NoArgsConstructor
@Entity
public class Visit {

    /**
     * Name for the DB sequence
     */
    private static final String SEQUENCE_NAME = "visit_sequence";

    /**
     * Visit's ID
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
    private Long id;

    /**
     * Doctor that has the visit
     */
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "doctor_id",
            foreignKey = @ForeignKey(name="FK_DOCTOR_DATA")
    )
    private DoctorData doctorData;

    /**
     * Client that has the visit
     */
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "client_id",
            foreignKey = @ForeignKey(name="FK_CLIENT_DATA")
    )
    private ClientData clientData;

    /**
     * Client's complaint with his health
     */
    @Column(nullable = false)
    private String complaint;

    /**
     * The time the visit is accepted
     */
    private LocalDateTime acceptedAt;

    /**
     * The time the visit occurs
     */
    private LocalDateTime appointsAt;

    /**
     * Non parsed time of the visit
     */
    @Transient
    private String appointsAtInput;

    /**
     * Status of the visit
     * @see VisitStatus
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VisitStatus status;

    /**
     * Parses appoint time from String to LocalDateTime and sets both of them to the fields
     * @param appointsAtInput string that is parsed to date and is set to the field
     * @see LocalDateTime
     */
    public void setAppointsAtInput(String appointsAtInput) {
        this.appointsAtInput = appointsAtInput;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        try {
            this.appointsAt = LocalDateTime.parse(appointsAtInput, formatter);

        } catch (DateTimeParseException ex) {
            appointsAt = LocalDateTime.MIN;

        }
    }

    /**
     * Creates visit object with doctor, client, complaint, confirmation time, appointment time and status
     * @param doctorData Doctor that has the visit
     * @param clientData Client that has the visit
     * @param complaint Client's complaint
     * @param acceptedAt Time of the visit's confirmation
     * @param appointsAt Time of the visit's appointment time
     * @param appointsAtInput Appointment time input
     * @param status Visit status
     */
    public Visit(
            DoctorData doctorData,
            ClientData clientData,
            String complaint,
            LocalDateTime acceptedAt,
            LocalDateTime appointsAt,
            String appointsAtInput,
            VisitStatus status) {

        this.doctorData = doctorData;
        this.clientData = clientData;
        this.complaint = complaint;
        this.acceptedAt = acceptedAt;
        this.appointsAt = appointsAt;
        this.appointsAtInput = appointsAtInput;
        this.status = status;
    }
}
