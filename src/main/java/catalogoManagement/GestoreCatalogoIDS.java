package catalogoManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import utenteManagement.PasswordUtils;

public class GestoreCatalogoIDS implements GestoreCatalogoDAO{
	
	private DataSource ds = null;
	private Connection connection = null;

	public GestoreCatalogoIDS(DataSource ds) {
		super();
		this.ds = ds;
		try {
			connection = ds.getConnection();
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
	}

	@Override
	public Boolean doSaveGestore(GestoreCatalogo gestoreCatalogo) throws SQLException {
		String query = "INSERT INTO " + GestoreCatalogoIDS.TABLE
				+ " (email_address, nome, cognome, password) VALUES (?, ?, ?, ?)";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			
			String hashedPassword = PasswordUtils.hashPassword(gestoreCatalogo.getPassword());


			preparedStatement.setString(1, gestoreCatalogo.getEmail());
			preparedStatement.setString(2, gestoreCatalogo.getNome());
			preparedStatement.setString(3, gestoreCatalogo.getCognome());
			preparedStatement.setString(4, hashedPassword);

			if(preparedStatement.executeUpdate()>0)
				return true;
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
		return false;
	}

	@Override
	public Boolean doUpdateGestore(GestoreCatalogo gestoreCatalogo) throws SQLException {
		String query = "UPDATE " + GestoreCatalogoIDS.TABLE
				+ "SET nome = ?, cognome = ?, password = ? "
				+ "WHERE email = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			
			String hashedPassword = PasswordUtils.hashPassword(gestoreCatalogo.getPassword());

			preparedStatement.setString(1, gestoreCatalogo.getNome());
			preparedStatement.setString(2, gestoreCatalogo.getCognome());
			preparedStatement.setString(3, hashedPassword);
			preparedStatement.setString(4, gestoreCatalogo.getEmail());

			if(preparedStatement.executeUpdate()>0)
			return true;
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
		
		return false;
		
	}

	@Override
	public Boolean doDeleteGestore(String email) throws SQLException {
		String query = "DELETE FROM " + GestoreCatalogoIDS.TABLE + " WHERE email_address = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {

			preparedStatement.setString(1, email);

			if(preparedStatement.executeUpdate()>0)
			return true;
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
		return false;
		
	}
	
	@Override
	public GestoreCatalogo doRetrieveByAuthentication(String email, String password) throws SQLException {
		String query = "SELECT * FROM " + GestoreCatalogoIDS.TABLE + " WHERE email_address = ? and password = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			String hashedPassword = PasswordUtils.hashPassword(password);
			
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, hashedPassword);

			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()) {
				String nome = rs.getString(NOME);
				String cognome = rs.getString(COGNOME);

				return new GestoreCatalogo(email, nome, cognome, hashedPassword);
			}

			rs.close();
			
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
		
		return null;
	}
	
	/*** MACRO ***/
	private static final String TABLE = "gestore_catalogo";
	private static final String NOME = "nome";
	private static final String COGNOME = "cognome";

	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(GestoreCatalogoIDS.class.getName());
	private static final String ERROR = "Errore";

}
