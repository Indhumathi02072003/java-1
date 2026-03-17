package com.ic.notification.contract;
import java.util.List;
public record SlackBlock(
        String type,
        SlackText text,
        List<SlackText> elements
) {
    public static SlackBlock section(SlackText text) {
        return new SlackBlock("section", text, null);
    }
    public static SlackBlock header(SlackText text) {
        return new SlackBlock("header", text, null);
    }
    public static SlackBlock divider() {
        return new SlackBlock("divider", null, null);
    }
    public static SlackBlock context(List<SlackText> elements) {
        return new SlackBlock("context", null, elements);
    }
}


