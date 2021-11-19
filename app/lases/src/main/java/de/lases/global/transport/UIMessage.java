package de.lases.global.transport;

/**
 * Holds the information necessary for constructing a FacesMessage. It is
 * contained in events used for displaying a message to the user.
 */
public class UIMessage {

    private final String message;

    private final MessageCategory messageCategory;

    /**
     * Construct a UIMessage with a message text and a category.
     *
     * @param message The message text.
     * @param messageCategory The message category.
     */
    public UIMessage(String message, MessageCategory messageCategory) {
        this.message = message;
        this.messageCategory = messageCategory;
    }

    /**
     * Get the text message for this {@code UIMessage}.
     *
     * @return The message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Get the category this message is in. For the options refer to
     * {@link MessageCategory}
     *
     * @return The message category.
     */
    public MessageCategory getCategory() {
        return messageCategory;
    }

}
