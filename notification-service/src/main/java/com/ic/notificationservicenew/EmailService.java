package com.ic.notificationservicenew;

import com.ic.notification.contract.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendEmail(NotificationEvent event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.recipient().email());
        message.setSubject(event.content().subject());
        message.setText(event.content().message());
        mailSender.send(message);
    }
}
