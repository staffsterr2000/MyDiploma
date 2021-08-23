package com.stasroshchenko.diploma.entity.user;

import com.google.common.collect.Sets;
import com.stasroshchenko.diploma.entity.person.ClientData;
import com.stasroshchenko.diploma.entity.person.DoctorData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Collection;

/**
 * Adapter-entity for user and doctor data entities
 * @author staffsterr2000
 * @version 1.0
 * @see ApplicationUser
 * @see DoctorData
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor
@DiscriminatorValue("DOCTOR")
@PrimaryKeyJoinColumn(foreignKey = @ForeignKey(name = "FK_DOCTOR_USER"))
@Entity
public class ApplicationUserDoctor extends ApplicationUser {

    /**
     * The data about doctor user
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            nullable = false,
            name = "doctor_data_id",
            foreignKey = @ForeignKey(name="FK_DOCTOR_DATA")
    )
    private DoctorData doctorData;

    /**
     * Gets all the doctor user's authorities
     * @return Collection of authorities the user has
     * @since 1.0
     * @see GrantedAuthority
     * @see SimpleGrantedAuthority
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Sets.newHashSet(new SimpleGrantedAuthority("ROLE_DOCTOR"));
    }

    /**
     * Creates a doctor user
     * @param doctorData User's data
     * @param username User's username
     * @param email User's email
     * @param password User's password
     */
    public ApplicationUserDoctor(
            DoctorData doctorData,
            String username,
            String email,
            String password) {

        super(username, email, password);
        this.doctorData = doctorData;
    }

}
