package com.stasroshchenko.diploma.auth;

import java.util.Optional;

public interface ApplicationUserDao {

    Optional<ApplicationUser> findByUsername(String username);

    Optional<ApplicationUser> findByEmail(String email);

}
