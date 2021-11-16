package de.lases.global.transport;

public class UIMessage {

    private final String message;

    private final Category category;

    public UIMessage(String message, Category n_category) {
        this.message = message;
        category = n_category;
    }

    public String getMessage() {
        return message;
    }

    public Category getCategory() {
        return category;
    }
}
