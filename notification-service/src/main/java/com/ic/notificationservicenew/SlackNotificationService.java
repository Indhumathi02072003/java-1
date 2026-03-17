package com.ic.notificationservicenew;

import com.ic.notification.contract.Content;
import com.ic.notification.contract.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlackNotificationService {

    private final WebClient webClient;

    public void sendMessage(NotificationEvent event) {

        Content content = event.content();

        if (content == null || content.blocks() == null || content.blocks().isEmpty()) {
            log.warn("No Slack blocks found for event {}", event);
            return;
        }

        if (event.recipient() == null || event.recipient().slackWebhookUrl() == null) {
            log.error("Missing Slack webhook URL for event {}", event);
            return;
        }

        String fallbackText =
                content.message() != null
                        ? content.message()
                        : "Slack notification";

        Map<String, Object> slackPayload = Map.of(
                "text", fallbackText,
                "blocks", content.blocks()
        );

        webClient.post()
                .uri(event.recipient().slackWebhookUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(slackPayload)
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error("Slack API error {}: {}", response.statusCode(), errorBody);
                                    return response.createException();
                                })
                )
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(3))
                .doOnSuccess(response ->
                        log.info("Slack message sent successfully"))
                .doOnError(ex ->
                        log.error("Failed to send Slack message for event {}", event, ex))
                .subscribe();
    }

}
