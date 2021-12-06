package de.lases.persistence.repository;

import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Transactions for repository operations.
 */
public class Transaction {

    private Connection connection;

    /**
     * Is the transaction already aborted or commited?
     */
    private boolean transactionOver;

    /**
     * Create a new Transaction.
     */
    public Transaction() {
        connection = ConnectionPool.getInstance().getConnection();
        transactionOver = false;
    }

    /**
     * Abort the transaction.
     * @throws IllegalStateException When the transaction is already over.
     */
    public void abort() {
        if (transactionOver)
            throw new IllegalStateException("Transaction is already over");
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        transactionOver = true;
        ConnectionPool.getInstance().releaseConnection(connection);
    }

    /**
     * Commit the transaction and write the changes to the repository.
     * @throws IllegalStateException When the transaction is already over.
     */
    public void commit() {
        if (transactionOver)
            throw new IllegalStateException("Transaction is already over");
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        transactionOver = true;
        ConnectionPool.getInstance().releaseConnection(connection);
    }

    /**
     * Get the db connection of the transaction.
     *
     * @return The db connection.
     * @throws IllegalStateException When the transaction is already over.
     */
    Connection getConnection() {
        if (transactionOver)
            throw new IllegalStateException("Transaction is already over");
        return connection;
    }

}
