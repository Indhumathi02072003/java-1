package com.ic.notification.contract;

public record SlackText(
        String type,   // "mrkdwn" or "plain_text"
        String text
) {}

