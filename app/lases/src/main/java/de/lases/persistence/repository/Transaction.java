package de.lases.persistence.repository;

import java.sql.Connection;

public class Transaction {

    private Connection connection;

    public void abort() {
        // begin: Code nur fuer diagramm
        ConnectionPool connPool = new ConnectionPool();
        // end: Code nur fuer diagramm
    }

    public void commit() {

    }

    public void close() {
    }

    Connection getConnection() {
        return null;
    }

}
