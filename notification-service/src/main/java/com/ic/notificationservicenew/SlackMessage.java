package com.ic.notificationservicenew;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ic.notification.contract.SlackBlock;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SlackMessage(List<SlackBlock> blocks) {}
