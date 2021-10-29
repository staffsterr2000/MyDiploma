package com.stasroshchenko.clinic.model.service.email;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Service for processing email business logic.
 * @author staffsterr2000
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class EmailService implements EmailSender {

    /**
     * Logging
     */
    private final static Logger LOGGER =
            LoggerFactory.getLogger(EmailService.class);



    private final static String FROM = "rosenkostas@gmail.com";



    /**
     * Mail sender
     */
    private final JavaMailSender mailSender;



    /**
     * Sends the email message to user.
     * @param to email address, where to send the email.
     * @param email message which will be sent.
     * @throws IllegalStateException if email hasn't been sent.
     * @since 1.0
     */
    @Override
    @Async
    public void send(String to, String email) {
        try {
            // creating
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");

            // setting
            helper.setText(email, true);           // контент листа
            helper.setTo(to);                           // отримувач листа
            helper.setSubject("Confirmation");          // тема листа
            helper.setFrom(FROM);          // відправляч листа

            // sending
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            LOGGER.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }

}
