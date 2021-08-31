package com.stasroshchenko.clinic.model.service;

import com.stasroshchenko.clinic.model.repository.ConfirmationTokenRepository;
import com.stasroshchenko.clinic.entity.ConfirmationToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Processes all confirmation token business logic.
 * @author staffsterr2000
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    /**
     * Token repo
     */
    private final ConfirmationTokenRepository confirmationTokenRepository;



    /**
     * Deletes certain token from DB.
     * @param token token to delete.
     * @since 1.0
     */
    public void deleteConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.delete(token);
    }



    /**
     * Saves token to DB.
     * @param token token to save.
     * @since 1.0
     */
    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }



    /**
     * Gets confirmation token by its UUID.
     * @param token UUID of the token we're looking for.
     * @return required token.
     * @throws IllegalStateException if token hasn't been found.
     * @since 1.0
     */
    public ConfirmationToken getConfirmationToken(String token) {
        return confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("No such token."));
    }



    /**
     * Gets list of all confirmation tokens.
     * @return list of all confirmation tokens.
     * @since 1.0
     */
    public List<ConfirmationToken> getAllConfirmationTokens() {
        return confirmationTokenRepository.findAll();
    }



    /**
     * Seeks for confirmation token and confirms it.
     * @param token UUID of the token we're looking for.
     * @since 1.0
     */
    public void setConfirmedAt(String token) {
        // get token by UUID
        getConfirmationToken(token)
                // confirm with current time
                .setConfirmedAt(LocalDateTime.now());
    }

}
