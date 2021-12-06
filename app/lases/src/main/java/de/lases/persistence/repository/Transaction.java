package de.lases.persistence.repository;

import de.lases.persistence.exception.DepletedResourceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Transactions for repository operations.
 */
public class Transaction {

    private Connection connection;

    private static final Logger logger
            = Logger.getLogger(Transaction.class.getName());

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
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                logSQLException(ex);
            }
            logSQLException(e);
            throw new DepletedResourceException("Transaction cannot"
                    + "be rolled back", e);
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
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.close();
                } catch (SQLException ex) {
                    logSQLException(ex);
                }
            }
            logSQLException(e);
            throw new DepletedResourceException("Commit failed");
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

    private void logSQLException(SQLException sqlException) {
        logger.log(Level.SEVERE,
                """
                Message: %s
                SQLState: %s
                Vendor error code: %s
                """.formatted(sqlException.getMessage(),
                        sqlException.getSQLState(), sqlException.getErrorCode())
                );
    }

}
