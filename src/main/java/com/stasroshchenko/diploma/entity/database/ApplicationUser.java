package com.stasroshchenko.diploma.entity.database;

import com.stasroshchenko.diploma.entity.database.person.ClientData;
import com.stasroshchenko.diploma.entity.database.person.DoctorData;
import com.stasroshchenko.diploma.entity.database.person.PersonData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
public class ApplicationUser implements UserDetails {

    private static final String SEQUENCE_NAME = "application_user_sequence";

    @Id
    @SequenceGenerator(
            name = SEQUENCE_NAME,
            sequenceName = SEQUENCE_NAME
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = SEQUENCE_NAME
    )
    @EqualsAndHashCode.Exclude
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name="person_id", referencedColumnName="id")
//            @JoinColumn(name="person_first_name", referencedColumnName="firstName")
    })
    private PersonData personData;
    private String username;
    private String email;
    private String password;
    private boolean isAccountLocked;
    private boolean isEnabled = false;

    public ApplicationUser(PersonData personData,
                           String username,
                           String email,
                           String password
    ) {
        this.personData = personData;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
        String role = (personData instanceof ClientData) ?
                "CLIENT" : (personData instanceof DoctorData) ?
                "DOCTOR" : null;
        if (role == null) throw new IllegalStateException("User role is unknown");

        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isAccountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

}
