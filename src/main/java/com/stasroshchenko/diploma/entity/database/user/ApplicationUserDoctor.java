package com.stasroshchenko.diploma.entity.database.user;

import com.google.common.collect.Sets;
import com.stasroshchenko.diploma.entity.database.person.DoctorData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor

@DiscriminatorValue("DOCTOR")
@Entity
public class ApplicationUserDoctor extends ApplicationUser {

    public ApplicationUserDoctor(
            DoctorData doctorData,
            String username,
            String email,
            String password) {

        super(username, email, password);
        this.doctorData= doctorData;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            nullable = false,
            name = "doctor_id",
            foreignKey = @ForeignKey(name="FK_DOCTOR_DATA")
    )
    private DoctorData doctorData;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Sets.newHashSet(new SimpleGrantedAuthority("ROLE_DOCTOR"));
    }

}
