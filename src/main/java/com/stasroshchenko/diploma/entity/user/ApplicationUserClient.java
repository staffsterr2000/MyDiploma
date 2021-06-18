package com.stasroshchenko.diploma.entity.user;

import com.google.common.collect.Sets;
import com.stasroshchenko.diploma.entity.person.ClientData;
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
@NoArgsConstructor
@Data

@DiscriminatorValue("CLIENT")
@Entity
public class ApplicationUserClient extends ApplicationUser {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            nullable = false,
            name = "client_data_id",
            foreignKey = @ForeignKey(name="FK_CLIENT_DATA")
    )
    private ClientData clientData;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Sets.newHashSet(new SimpleGrantedAuthority("ROLE_CLIENT"));
    }

    public ApplicationUserClient(
            ClientData clientData,
            String username,
            String email,
            String password) {

        super(username, email, password);
        this.clientData = clientData;
    }
}
