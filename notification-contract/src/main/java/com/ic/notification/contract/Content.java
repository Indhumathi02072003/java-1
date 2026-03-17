package com.ic.notification.contract;

import java.util.List;

public record Content(
    // Common
    String title,
    String message,
    // Email-specific
    String subject,
    boolean html,
    // Slack-specific
    List<SlackBlock> blocks
) {}
