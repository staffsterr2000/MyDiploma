package com.stasroshchenko.diploma.repository;

import com.stasroshchenko.diploma.entity.database.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationUserRepository
        extends JpaRepository<ApplicationUser, Integer> {

    Optional<ApplicationUser> findByUsername(String username);

    Optional<ApplicationUser> findByEmail(String email);
}
