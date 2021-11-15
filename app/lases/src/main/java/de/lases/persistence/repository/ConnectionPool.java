package de.lases.persistence.repository;

import java.sql.Connection;

class ConnectionPool {

    static ConnectionPool getInstance() {
        return null;
    }

    Connection getConnection() {
        return null;
    }

    void releaseConnection(Connection connection) {
    }

    public static void init() {

    }

}
