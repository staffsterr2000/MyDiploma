package com.stasroshchenko.diploma.entity.database;

import com.stasroshchenko.diploma.entity.database.person.ClientData;
import com.stasroshchenko.diploma.entity.database.person.DoctorData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Visit {

    private static final String SEQUENCE_NAME = "visit_sequence";

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

    @OneToOne
    @JoinColumn(
            nullable = false,
            name = "doctor_id",
            foreignKey = @ForeignKey(name="FK_DOCTOR_DATA")
    )
    private DoctorData doctor;

    @OneToOne
    @JoinColumn(
            nullable = false,
            name = "client_id",
            foreignKey = @ForeignKey(name="FK_CLIENT_DATA")
    )
    private ClientData clientData;

    @Column(nullable = false)
    private String complaint;
    private LocalDateTime acceptedAt;
    private LocalDateTime time;
    private Boolean isOccurred;

}
