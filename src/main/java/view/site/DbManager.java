package view.site;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
public class DbManager {
	private List<Connection> freeDbConnections;

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("DB driver not found:" + e.getMessage());
		}
	}

	public DbManager() {
		freeDbConnections = new LinkedList<Connection>();
	}

	private static DbManager instance;

	private synchronized Connection createDBConnection() throws SQLException {
		Connection newConnection = null;
		String ip = "localhost";
		String port = "3306";
		String db = "kawaiiComix";
		String username = "root";
		String password = "vittismo13";

		newConnection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + db
				+ "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
				username, password);
		newConnection.setAutoCommit(false);
		return newConnection;
	}

	public synchronized Connection getConnection() throws SQLException {
		Connection connection;

		if (!freeDbConnections.isEmpty()) {
			connection = (Connection) freeDbConnections.get(0);
			freeDbConnections.remove(0);

			try {
				if (connection.isClosed())
					connection = getConnection();
			} catch (SQLException e) {
				connection.close();
				connection = getConnection();
			}
		} else {
			connection = createDBConnection();
		}

		return connection;
	}

	public static synchronized DbManager getInstance() {
		if (instance == null) {
			instance = new DbManager();
		}
		return instance;
	}

	public synchronized void releaseConnection(Connection connection) throws SQLException {
		if (connection != null)
			freeDbConnections.add(connection);
	}
}
