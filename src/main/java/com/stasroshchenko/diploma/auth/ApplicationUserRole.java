package com.stasroshchenko.diploma.auth;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.stasroshchenko.diploma.auth.ApplicationUserPermission.*;

@AllArgsConstructor
public enum ApplicationUserRole {
    ADMIN(Sets.newHashSet(READ, WRITE)),
    CLIENT(Sets.newHashSet());

    private final Set<ApplicationUserPermission> permissions;

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<? extends GrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> grantedAuthorities = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.toString()))
                .collect(Collectors.toSet());
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.toString()));
        return grantedAuthorities;
    }
}
