package de.lases.persistence.repository;

import de.lases.persistence.exception.DatasourceQueryFailedException;
import de.lases.persistence.util.DatasourceUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Transactions for repository operations.
 *
 * @author Sebastian Vogt
 */
public class Transaction {

    private final Connection connection;

    private static final Logger logger
            = Logger.getLogger(Transaction.class.getName());

    private final ScheduledExecutorService executor;

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

        StackTraceElement lastStackTraceElement = Thread.currentThread().getStackTrace()[2];
        logger.log(Level.INFO, "Transaction " + this.hashCode()
                + " was opened. The connection pool has: " + ConnectionPool.getInstance().getNumberOfFreeConnections()
                + " free connections of " + ConnectionPool.POOL_SIZE + ". I was opened by "
                + lastStackTraceElement.getMethodName() + " in class "
                + lastStackTraceElement.getClassName() + " on line " + lastStackTraceElement.getLineNumber());

        Runnable logOpenTransaction = () -> logger.log(Level.INFO, "Transaction " + this.hashCode()
                + " is still open.");
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(logOpenTransaction, 3, 3, TimeUnit.SECONDS);
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
                connection.close();
            } catch (SQLException ex) {
                DatasourceUtil.logSQLException(ex, logger);
            }
            DatasourceUtil.logSQLException(e, logger);
            throw new DatasourceQueryFailedException("Transaction cannot"
                    + "be rolled back", e);
        }
        transactionOver = true;
        ConnectionPool.getInstance().releaseConnection(connection);

        logger.log(Level.INFO, "Transaction " + this.hashCode()
                + " was closed. The connection pool has: " + ConnectionPool.getInstance().getNumberOfFreeConnections()
                + " free connections of " + ConnectionPool.POOL_SIZE);
        executor.shutdown();
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
            try {
                connection.rollback();
                connection.close();
            } catch (SQLException ex) {
                DatasourceUtil.logSQLException(ex, logger);
            }
            DatasourceUtil.logSQLException(e, logger);
            throw new DatasourceQueryFailedException("Commit failed");
        }
        transactionOver = true;
        ConnectionPool.getInstance().releaseConnection(connection);

        logger.log(Level.INFO, "Transaction " + this.hashCode()
                + " was closed. The connection pool has: " + ConnectionPool.getInstance().getNumberOfFreeConnections()
                + " free connections of " + ConnectionPool.POOL_SIZE);
        executor.shutdown();
    }

    /**
     * Get the db connection of the transaction.
     *
     * @return The db connection.
     * @throws IllegalStateException When the transaction is already over.
     */
    public Connection getConnection() {
        if (transactionOver)
            throw new IllegalStateException("Transaction is already over");
        return connection;
    }

}
