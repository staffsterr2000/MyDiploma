package com.stasroshchenko.diploma.model.repository;

import com.stasroshchenko.diploma.entity.user.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repo for working with users.
 * @author staffsterr2000
 * @version 1.0
 * @see ApplicationUser
 */
@Repository
public interface ApplicationUserRepository
        extends JpaRepository<ApplicationUser, Integer> {

    /**
     * Finds application user by its username.
     * The search ignores the username's case.
     * @param username username of the user we're looking for.
     * @return required user in Optional wrapper.
     * @since 1.0
     */
    @Query("SELECT au FROM ApplicationUser au WHERE LOWER(au.username) = LOWER(?1)")
    Optional<ApplicationUser> findByUsername(String username);

    /**
     * Finds application user by its email.
     * The search ignores the email's case.
     * @param email email of the user we're looking for.
     * @return required user in Optional wrapper.
     * @since 1.0
     */
    @Query("SELECT au FROM ApplicationUser au WHERE LOWER(au.email) = LOWER(?1)")
    Optional<ApplicationUser> findByEmail(String email);

}
