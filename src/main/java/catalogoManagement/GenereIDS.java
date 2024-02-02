package catalogoManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class GenereIDS implements GenereDAO{
	
	private DataSource ds = null;
	private Connection connection;

	public GenereIDS(DataSource ds) {
		super();
		this.ds = ds;
		
		try {
			connection = ds.getConnection();
		} catch (SQLException e) {

			logger.log(Level.ALL, ERROR, e);
		}
	}

	@Override
	public Collection<String> doRetrieveAll() throws SQLException {
		String query = "SELECT * FROM " + GenereIDS.TABLE;
		ArrayList<String> generi = new ArrayList<>();

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) 
				generi.add(rs.getString("nome"));
			
			rs.close();
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}

		return generi;
	}

	@Override
	public Boolean checkGenereName(String genereName) throws SQLException {
		String query = "SELECT * FROM " + GenereIDS.TABLE+ " WHERE nome = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setString(1,  genereName);
			ResultSet rs = preparedStatement.executeQuery();
			
			if (rs.next()) {
				rs.close();
				return true;
			}
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}

		return false;
	}
	
	/*** MACRO ***/
	private static final String TABLE = "genere";
	
	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(GenereIDS.class.getName());
	private static final String ERROR = "Errore";
}
