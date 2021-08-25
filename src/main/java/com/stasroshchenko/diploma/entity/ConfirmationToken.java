package com.stasroshchenko.diploma.entity;

import com.stasroshchenko.diploma.entity.user.ApplicationUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents verification token entity, is used for enable user's account.
 * @author staffsterr2000
 * @version 1.0
 * @see ApplicationUser
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {

    /**
     * Name for the DB sequence
     */
    private static final String SEQUENCE_NAME = "confirmation_token_sequence";



    /**
     * Token's ID
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
     * User, that has to confirm it, unless it is expired
     */
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id",
            foreignKey = @ForeignKey(name="FK_APP_USER")
    )
    private ApplicationUser applicationUser;

    /**
     * Token's UUID
     */
    @Column(nullable = false)
    private String token;

    /**
     * Token's time of creation
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * Token's time of expiration
     */
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    /**
     * Token's time of confirmation
     */
    private LocalDateTime confirmedAt;



    /**
     * Creates token with UUID, user, creation time and expiration time
     * @param token UUID
     * @param applicationUser the user that received this token
     * @param createdAt time of the token's creation
     * @param expiresAt time of the token's expiration
     */
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
