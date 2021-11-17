package de.lases.global.transport;

/**
 * Holds all data that is read from the config file.
 */
public class GlobalConfig {

    private int maximumMegabytesPDF;

    private int maximumMegabytesAvatar;

    private int lengthOfPaginatedLists;

    private String senderEmail;

    private String SMTPUsername;

    private String SMTPPassword;

    private String dbHostname;

    private String dbName;

    private String dbUsername;

    private String dbPassword;

    public int getMaximumMegabytesPDF() {
        return maximumMegabytesPDF;
    }

    /**
     * Set the maximum number of megabytes a pdf can have in the system.
     * @param maximumMegabytesPDF The maximum number of megabytes for a pdf.
     */
    public void setMaximumMegabytesPDF(int maximumMegabytesPDF) {
        this.maximumMegabytesPDF = maximumMegabytesPDF;
    }

    public int getMaximumMegabytesAvatar() {
        return maximumMegabytesAvatar;
    }

    /**
     * Set the maximum number of megabytes an uploaded avatar can have.
     *
     * @param maximumMegabytesAvatar Maximum number of megabytes an uploaded
     *                               avatar can have.
     */
    public void setMaximumMegabytesAvatar(int maximumMegabytesAvatar) {
        this.maximumMegabytesAvatar = maximumMegabytesAvatar;
    }

    public int getLengthOfPaginatedLists() {
        return lengthOfPaginatedLists;
    }

    /**
     * Set how many entries a paginated list in the system has on one page.
     *
     * @param lengthOfPaginatedLists Number of entries for a paginated list.
     */
    public void setLengthOfPaginatedLists(int lengthOfPaginatedLists) {
        this.lengthOfPaginatedLists = lengthOfPaginatedLists;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    /**
     * Set the sender email address for system emails.
     *
     * @param senderEmail Sender address for system emails.
     */
    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSMTPUsername() {
        return SMTPUsername;
    }

    /**
     * Set the username for the SMTP server used for system emails.
     *
     * @param SMTPUsername Username for the SMTP server.
     */
    public void setSMTPUsername(String SMTPUsername) {
        this.SMTPUsername = SMTPUsername;
    }

    public String getSMTPPassword() {
        return SMTPPassword;
    }

    /**
     * Set the password for the SMTP server used for system emails.
     *
     * @param SMTPPassword Username for the SMTP server.
     */
    public void setSMTPPassword(String SMTPPassword) {
        this.SMTPPassword = SMTPPassword;
    }

    public String getDbHostname() {
        return dbHostname;
    }

    /**
     * Set the hostname for the database used by the system.
     *
     * @param dbHostname Hostname for the database.
     */
    public void setDbHostname(String dbHostname) {
        this.dbHostname = dbHostname;
    }

    public String getDbName() {
        return dbName;
    }

    /**
     * Set the name for the database used by the system.
     *
     * @param dbName Name for the databse.
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    /**
     * Set the username for the database used by the system.
     *
     * @param dbUsername Username for the databse.
     */
    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    /**
     * Set the password for the database used bu the system.
     *
     * @param dbPassword Password for the database.
     */
    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }
}
