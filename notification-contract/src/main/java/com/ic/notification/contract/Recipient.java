package com.ic.notification.contract;

public record Recipient(
    String email,
    String slackChannel,
    String slackWebhookUrl,
    String phoneNumber, // sms with country code e.g., +1234567890
    String whatsAppNumber // whatsapp with country code e.g., +1234567890
) {}
