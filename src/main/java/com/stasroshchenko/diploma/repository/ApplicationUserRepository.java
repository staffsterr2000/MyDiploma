package com.stasroshchenko.diploma.repository;

import com.stasroshchenko.diploma.entity.database.user.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationUserRepository
        extends JpaRepository<ApplicationUser, Integer> {

    // ignores case
    @Query("SELECT au FROM ApplicationUser au WHERE LOWER(au.username) = LOWER(?1)")
    Optional<ApplicationUser> findByUsername(String username);

    // ignores case
    @Query("SELECT au FROM ApplicationUser au WHERE LOWER(au.email) = LOWER(?1)")
    Optional<ApplicationUser> findByEmail(String email);

}
