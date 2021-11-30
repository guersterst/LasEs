package de.lases.persistence.repository;

import jakarta.inject.Inject;

import java.sql.Connection;

/**
 * Transactions for repository operations.
 */
public class Transaction {

    private Connection connection;

    /**
     * Create a new Transaction.
     */
    public Transaction() {
    }

    /**
     * Abort the transaction.
     */
    public void abort() {
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
