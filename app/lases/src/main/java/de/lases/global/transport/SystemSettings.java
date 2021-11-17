package de.lases.global.transport;

/**
 * Represents the system settings.
 */
public class SystemSettings {

    private String imprint;

    private String companyName;

    private String headlineWelcomePage;

    private String messageWelcomePage;

    private Style style;

    public String getImprint() {
        return imprint;
    }

    /**
     * Set the imprint that should be displayed on the imprint page.
     *
     * @param imprint The imprint text.
     */
    public void setImprint(String imprint) {
        this.imprint = imprint;
    }

    public String getCompanyName() {
        return companyName;
    }

    /**
     * Set the name of the company who runs the application.
     *
     * @param companyName The name of the company.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getHeadlineWelcomePage() {
        return headlineWelcomePage;
    }

    /**
     * Set the headline that should be displayed on the welcome page.
     *
     * @param headlineWelcomePage
     */
    public void setHeadlineWelcomePage(String headlineWelcomePage) {
        this.headlineWelcomePage = headlineWelcomePage;
    }

    public String getMessageWelcomePage() {
        return messageWelcomePage;
    }

    /**
     * Set the message that should be displayed on the welcome page to all
     * users.
     *
     * @param messageWelcomePage The message text for the welcome page.
     */
    public void setMessageWelcomePage(String messageWelcomePage) {
        this.messageWelcomePage = messageWelcomePage;
    }

    public Style getStyle() {
        return style;
    }

    /**
     * Set the global style option for the application. For all choices refer
     * to {@link Style}
     *
     * @param style The global style of the application.
     */
    public void setStyle(Style style) {
        this.style = style;
    }
}
