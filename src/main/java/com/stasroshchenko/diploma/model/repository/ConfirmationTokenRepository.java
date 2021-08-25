package com.stasroshchenko.diploma.model.repository;

import com.stasroshchenko.diploma.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for confirmation tokens.
 * @author staffsterr2000
 * @version 1.0
 * @see ConfirmationToken
 */
@Repository
public interface ConfirmationTokenRepository
        extends JpaRepository<ConfirmationToken, Long> {

    /**
     * Finds token by its UUID.
     * @param token UUID
     * @return confirmation token
     * @since 1.0
     */
    Optional<ConfirmationToken> findByToken(String token);

}
