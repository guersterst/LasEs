package de.lases.global.transport;

/**
 * Encapsulates information about the state of the datasource connection in
 * order to show it to the admin on the first setup page.
 */
public class ConnectionState {

    private boolean successfullyConnected;

    private String errorMessage;

    private boolean isDatasourceSchemaCreated;

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

    public boolean isDatasourceSchemaCreated() {
        return isDatasourceSchemaCreated;
    }

    /**
     * Set whether the datasource has been instantiated.
     * For the case of a SQL Database, this means whether the relations have been created via {@code CREATE TABLE...}.
     *
     * @param datasourceSchemaCreated Boolean on whether the creation has already happened.
     */
    public void setDatasourceSchemaCreated(boolean datasourceSchemaCreated) {
        isDatasourceSchemaCreated = datasourceSchemaCreated;
    }
}
