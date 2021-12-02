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
     * @throws IllegalStateException When the transaction is already over.
     */
    public void abort() {
    }

    /**
     * Commit the transaction and write the changes to the repository.
     * @throws IllegalStateException When the transaction is already over.
     */
    public void commit() {

    }

    /**
     * Get the db connection of the transaction.
     *
     * @return The db connection.
     * @throws IllegalStateException When the transaction is already over.
     */
    Connection getConnection() {
        return null;
    }

}
