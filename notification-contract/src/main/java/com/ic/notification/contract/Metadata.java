package com.ic.notification.contract;

import java.time.Instant;

public record Metadata(
    String eventId,
    String sourceService,
    Instant createdAt,
    int retryCount
) {}
