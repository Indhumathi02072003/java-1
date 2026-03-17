package com.ic.notification.contract;

import jakarta.validation.constraints.NotNull;

public record NotificationEvent(
    @NotNull NotificationType type,
    @NotNull Recipient recipient,
    @NotNull Content content,
    Metadata metadata
) {}
