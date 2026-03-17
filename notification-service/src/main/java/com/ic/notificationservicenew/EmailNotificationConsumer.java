package com.ic.notificationservicenew;

import com.ic.notification.contract.NotificationEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationConsumer {

    private final EmailService emailService;

    public EmailNotificationConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "${topics.email}")
    public void consume(NotificationEvent event) {
        emailService.sendEmail(event);
    }
}
