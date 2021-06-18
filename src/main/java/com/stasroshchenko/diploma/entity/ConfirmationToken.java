package com.stasroshchenko.diploma.entity;

import com.stasroshchenko.diploma.entity.user.ApplicationUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class ConfirmationToken {

    private static final String SEQUENCE_NAME = "confirmation_token_sequence";

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

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id",
            foreignKey = @ForeignKey(name="FK_APP_USER")
    )
    private ApplicationUser applicationUser;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    public ConfirmationToken(String token,
                             ApplicationUser applicationUser,
                             LocalDateTime createdAt,
                             LocalDateTime expiresAt) {
        this.token = token;
        this.applicationUser = applicationUser;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }


}
