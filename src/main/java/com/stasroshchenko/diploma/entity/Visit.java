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

@Data
@NoArgsConstructor

@Entity
public class Visit {

    private static final String SEQUENCE_NAME = "visit_sequence";

    @Id
//    @SequenceGenerator(
//            name = SEQUENCE_NAME,
//            sequenceName = SEQUENCE_NAME,
//            allocationSize = 1
//    )
    @GeneratedValue(
//            strategy = GenerationType.SEQUENCE,
            strategy = GenerationType.IDENTITY,
            generator = SEQUENCE_NAME
    )
    private Long id;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "doctor_id",
            foreignKey = @ForeignKey(name="FK_DOCTOR_DATA")
    )
    private DoctorData doctorData;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "client_id",
            foreignKey = @ForeignKey(name="FK_CLIENT_DATA")
    )
    private ClientData clientData;

    @Column(nullable = false)
    private String complaint;
    private LocalDateTime acceptedAt;
    private LocalDateTime appointsAt;

    @Transient
    private String appointsAtInput;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VisitStatus status;

    public void setAppointsAtInput(String appointsAtInput) {
        this.appointsAtInput = appointsAtInput;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        try {
            this.appointsAt = LocalDateTime.parse(appointsAtInput, formatter);

        } catch (DateTimeParseException ex) {
            appointsAt = LocalDateTime.MIN;

        }
    }

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
