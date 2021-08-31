package com.stasroshchenko.clinic.entity.user;

import com.google.common.collect.Sets;
import com.stasroshchenko.clinic.entity.person.ClientData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Collection;

/**
 * Adapter-entity for user and client data entities.
 * @author staffsterr2000
 * @version 1.0
 * @see ApplicationUser
 * @see ClientData
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@NoArgsConstructor
@DiscriminatorValue("CLIENT")
@PrimaryKeyJoinColumn(foreignKey = @ForeignKey(name = "FK_CLIENT_USER"))
@Entity
public class ApplicationUserClient extends ApplicationUser {

    /**
     * The data about the client user
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            nullable = false,
            name = "client_data_id",
            foreignKey = @ForeignKey(name="FK_CLIENT_DATA")
    )
    private ClientData clientData;




    /**
     * Gets all the client user's authorities
     * @return collection of authorities the user has
     * @since 1.0
     * @see GrantedAuthority
     * @see SimpleGrantedAuthority
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Sets.newHashSet(new SimpleGrantedAuthority("ROLE_CLIENT"));
    }




    /**
     * Creates a client user
     * @param clientData user's data
     * @param username user's username
     * @param email user's email
     * @param password user's password
     */
    public ApplicationUserClient(
            ClientData clientData,
            String username,
            String email,
            String password) {

        super(username, email, password);
        this.clientData = clientData;
    }
}
