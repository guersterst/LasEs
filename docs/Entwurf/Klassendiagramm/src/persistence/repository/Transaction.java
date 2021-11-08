package persistence.repository;

import java.sql.Connection;
import persistence.repository.connection_pool.aw4r;

public class Transaction extends aw4r {

	private Connection connection;

	public void abort() {
		// begin: Code nur fuer diagramm
		ConnectionPool connPool = new ConnectionPool();
		// end: Code nur fuer diagramm
	}

	public void commit() {
	
	}

	Connection getConnection() {
		return null;
	}

}
