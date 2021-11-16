package de.lases.global.transport;

/**
 * Encapsulates information about the state of the database connection.
 */
public class ConnectionState {

    private boolean successfullyConnected;

    private String errorMessage;

    public boolean isSuccessfullyConnected() {
        return successfullyConnected;
    }

    /**
     * Set the value that states if the data source is successfully connected.
     *
     * @param successfullyConnected Is the data source successfully connected.
     */
    public void setSuccessfullyConnected(boolean successfullyConnected) {
        this.successfullyConnected = successfullyConnected;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Set the error message that gives information about why the data source
     * is not successfully connected.
     *
     * @param errorMessage Error message that states why the data source is not
     *                     successfully connected.
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
