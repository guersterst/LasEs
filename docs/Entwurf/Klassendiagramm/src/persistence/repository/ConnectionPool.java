package persistence.repository;

import javax.xml.registry.Connection;

class ConnectionPool {
	
	static ConnectionPool getInstance() { return null; }
	
	Connection getConnection() { return null; }
	
	void releaseConnection(Connection connection) { }

	public static void init() {
	
	}

}
