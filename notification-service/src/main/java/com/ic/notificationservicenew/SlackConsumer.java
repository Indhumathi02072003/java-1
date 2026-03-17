package com.ic.notificationservicenew;

import com.ic.notification.contract.NotificationEvent;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SlackConsumer {

    private SlackNotificationService slackNotificationService;

    @KafkaListener(topics = "${topics.slack}")
    public void consume(NotificationEvent event) {

        slackNotificationService.sendMessage(event);
    }
}