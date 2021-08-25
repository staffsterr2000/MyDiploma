package com.stasroshchenko.diploma.entity.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

/**
 * Represents user entity, to enter the clinic profile.
 * @author staffsterr2000
 * @version 1.0
 * @see UserDetails
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "application_user")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "ROLE")
public abstract class ApplicationUser implements UserDetails {

    /**
     * Name for the DB sequence
     */
    private static final String SEQUENCE_NAME = "application_user_sequence";



    /**
     * User's ID, isn't in use while procedure of equaling and hashing
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
    protected Long id;

    /**
     * User's username
     */
    @Column(nullable = false)
    protected String username;

    /**
     * User's email
     */
    @Column(nullable = false)
    protected String email;

    /**
     * User's password
     */
    @Column(nullable = false)
    protected String password;

    /**
     * Link to user's avatar image (has default value)
     */
    @Column(nullable = false)
    protected String imageLink = "default.jpg";

    /**
     * Shows the user's lock status
     * true - the user's account is locked
     */
    @Column(nullable = false)
    protected boolean isAccountLocked;

    /**
     * Shows the user's enable status
     * true - the user's account is enabled
     */
    @Column(nullable = false)
    protected boolean isEnabled = false;



    /**
     * Creates user with username, email and password
     * @param username user's username
     * @param email user's email
     * @param password user's password
     */
    public ApplicationUser(
            String username,
            String email,
            String password
    ) {
        this.username = username;
        this.email = email;
        this.password = password;
    }



    /**
     * Gets the user's password
     * @return user's password
     * @since 1.0
     */
    @Override
    public String getPassword() {
        return password;
    }



    /**
     * Gets the user's username
     * @return user's username
     * @since 1.0
     */
    @Override
    public String getUsername() {
        return username;
    }



    /**
     * Gets the user's status of expiration
     * true - the user's account isn't expired
     * @return user's status of expiration
     * @since 1.0
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }



    /**
     * Gets the user's lock status
     * true - the user's account isn't locked
     * @return user's lock status
     * @since 1.0
     */
    @Override
    public boolean isAccountNonLocked() {
        return !isAccountLocked;
    }



    /**
     * Gets the user's credentials expiration status
     * true - the user's credentials aren't expired
     * @return user's credentials expiration status
     * @since 1.0
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }



    /**
     * Gets the user's enable status
     * true - the user's email is verified
     * @return user's credentials expiration status
     * @since 1.0
     */
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

}
