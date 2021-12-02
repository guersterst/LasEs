package de.lases.global.transport;

/**
 * Represents the system settings.
 */
public class SystemSettings implements Cloneable {

    private String imprint;

    private String companyName;

    private String headlineWelcomePage;

    private String messageWelcomePage;

    private String style;

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
     * @param headlineWelcomePage The headline that should be displayed on the
     *                            welcome page.
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

    public String getStyle() {
        return style;
    }

    /**
     * Set the global style option for the application as a css name.
     *
     * @param style The global style of the application.
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * Create a deep copy of the original object.
     *
     * @return A deep copy.
     */
    @Override
    public SystemSettings clone() {
        try {
            SystemSettings clone = (SystemSettings) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
