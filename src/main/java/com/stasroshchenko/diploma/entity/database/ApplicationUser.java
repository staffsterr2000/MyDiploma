package com.stasroshchenko.diploma.entity.database;

import com.stasroshchenko.diploma.auth.ApplicationUserRole;
import com.stasroshchenko.diploma.entity.PersonData;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Data
@NoArgsConstructor
@Entity
public class ApplicationUser implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "client_sequence",
            sequenceName = "client_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "client_sequence"
    )
    private Long id;

    @Enumerated(EnumType.STRING)
    private ApplicationUserRole role;

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

    public ApplicationUser(ApplicationUserRole role,
                           PersonData personData,
                           String username,
                           String email,
                           String password
    ) {
        this.role = role;
        this.personData = personData;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getGrantedAuthorities();
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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        ApplicationUser that = (ApplicationUser) o;
//        return isAccountLocked == that.isAccountLocked &&
//                isEnabled == that.isEnabled &&
//                role == that.role &&
//                username.equals(that.username) &&
//                email.equals(that.email) &&
//                password.equals(that.password);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(role, username, email, password, isAccountLocked, isEnabled);
//    }
}
