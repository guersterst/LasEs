package de.lases.global.transport;

/**
 * Holds the information necessary for constructing a FacesMessage. It is
 * contained in events used for displaying a message to the user.
 */
public class UIMessage {

    private final String message;

    private final MessageCategory messageCategory;

    public UIMessage(String message, MessageCategory messageCategory) {
        this.message = message;
        this.messageCategory = messageCategory;
    }

    public String getMessage() {
        return message;
    }

    public MessageCategory getCategory() {
        return messageCategory;
    }
}
