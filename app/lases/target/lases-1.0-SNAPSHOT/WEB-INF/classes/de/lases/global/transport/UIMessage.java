package de.lases.global.transport;

public class UIMessage {

    private final String message;

    private final Category category;

    public UIMessage(String message, Category category) {
        this.message = message;
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    public Category getCategory() {
        return category;
    }
}
