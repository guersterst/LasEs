package de.lases.persistence.repository;

import java.sql.Connection;

/**
 * Transactions for repository operations.
 */
public class Transaction {

    private Connection connection;

    /**
     * Abort the transaction.
     */
    public void abort() {
        // begin: Code nur fuer diagramm
        ConnectionPool connPool = new ConnectionPool();
        // end: Code nur fuer diagramm
    }

    /**
     * Commit the transaction and write the changes to the repository.
     */
    public void commit() {

    }

    /**
     * Get the db connection of the transaction.
     *
     * @return The db connection.
     */
    Connection getConnection() {
        return null;
    }

}
