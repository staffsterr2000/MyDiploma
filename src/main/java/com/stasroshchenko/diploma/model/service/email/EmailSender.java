package com.stasroshchenko.diploma.model.service.email;

/**
 * Used for sending emails.
 * @author staffsterr2000
 * @version 1.0
 */
public interface EmailSender {

    /**
     * Sends the email message to user.
     * @param to email address, where to send the email.
     * @param email message which will be sent.
     * @since 1.0
     */
    void send(String to, String email);

}
