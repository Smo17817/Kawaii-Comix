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

	public CategoriaIDS(DataSource ds) {
		super();
		this.ds = ds;
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
			logger.log(Level.ALL, error, e);
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
			logger.log(Level.ALL, error, e);
		}

		return false;
	}
	
	/*** MACRO ***/
	private static final String TABLE = "genere";
	
	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(GenereIDS.class.getName());
	private static final String error = "Errore";
}
