package com.stasroshchenko.diploma.model.service;

import com.stasroshchenko.diploma.entity.ConfirmationToken;
import com.stasroshchenko.diploma.model.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void deleteConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.delete(token);
    }

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getConfirmationToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public List<ConfirmationToken> getAllConfirmationTokens() {
        return confirmationTokenRepository.findAll();
    }

    public void setConfirmedAt(String token) {
        getConfirmationToken(token)
                .orElseThrow(() -> new IllegalStateException("no such token"))
                .setConfirmedAt(LocalDateTime.now());
    }
}
