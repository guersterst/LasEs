package de.uni_passau.fim.einfuehrung.einfuehrungsaufgabe_garstenauer_2.model;

public class UIMessage {

    private final String message;

    private final Category category;

    public enum Category {
        ERROR,
        INFO,
        WARNING,
        FATAL
    }

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
