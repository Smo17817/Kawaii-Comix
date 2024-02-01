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

public class CategoriaIDS implements CategoriaDAO{
	
	private DataSource ds = null;
	private Connection connection;

	public CategoriaIDS(DataSource ds) {
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
		String query = "SELECT * FROM " + CategoriaIDS.TABLE;
		ArrayList<String> categorie = new ArrayList<>();

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) 
				categorie.add(rs.getString("nome"));
			
			rs.close();
			return categorie;
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}

		return categorie;
	}

	@Override
	public Boolean checkCategoriaName(String categoriaName) throws SQLException{
		String query = "SELECT * FROM " + CategoriaIDS.TABLE+ " WHERE nome = ?";
		
		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setString(1,  categoriaName);
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
	private static final String TABLE = "categoria";
	
	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(CategoriaIDS.class.getName());
	private static final String ERROR = "Errore";
}
