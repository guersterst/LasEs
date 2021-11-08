package persistence.repository.connection_pool;

import javax.xml.registry.Connection;

public class ConnectionPool {
	
	public static ConnectionPool getInstance() { return null; }
	
	public Connection getConnection() { return null; }
	
	public void releaseConnection(Connection connection) { }

}
