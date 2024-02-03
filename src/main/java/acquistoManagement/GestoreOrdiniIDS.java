package acquistoManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import utenteManagement.PasswordUtils;

public class GestoreOrdiniIDS implements GestoreOrdiniDAO{
	
	private DataSource ds = null;
	private Connection connection = null;

	public GestoreOrdiniIDS(DataSource ds) {
		super();
		this.ds = ds;
		try {
			connection = ds.getConnection();
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
		
	}

	@Override
	public Boolean doSaveGestore(GestoreOrdini gestoreOrdini) throws SQLException {
		String query = "INSERT INTO " + GestoreOrdiniIDS.TABLE
				+ " (email_address, nome, cognome, password) VALUES (?, ?, ?, ?)";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {

			String hashedPassword = PasswordUtils.hashPassword(gestoreOrdini.getPassword());

			
			preparedStatement.setString(1, gestoreOrdini.getEmail());
			preparedStatement.setString(2, gestoreOrdini.getNome());
			preparedStatement.setString(3, gestoreOrdini.getCognome());
			preparedStatement.setString(4, hashedPassword);

			if(preparedStatement.executeUpdate()>0)
				return true;
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
		return false;
	}

	@Override
	public Boolean doUpdateGestore(GestoreOrdini gestoreOrdini) throws SQLException {
		String query = "UPDATE " + GestoreOrdiniIDS.TABLE
				+ "SET nome = ?, cognome = ?, password = ? "
				+ "WHERE email = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			
			String hashedPassword = PasswordUtils.hashPassword(gestoreOrdini.getPassword());

			preparedStatement.setString(1, gestoreOrdini.getNome());
			preparedStatement.setString(2, gestoreOrdini.getCognome());
			preparedStatement.setString(3, hashedPassword);
			preparedStatement.setString(4, gestoreOrdini.getEmail());

			if(preparedStatement.executeUpdate()>0)
			return true;
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
		
		return false;
		
	}

	@Override
	public Boolean doDeleteGestore(String email) throws SQLException {
		String query = "DELETE FROM " + GestoreOrdiniIDS.TABLE + " WHERE email_address = ?";

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
	public GestoreOrdini doRetrieveByAuthentication(String email, String password) throws SQLException {
		String query = "SELECT * FROM " + GestoreOrdiniIDS.TABLE + " WHERE email_address = ? and password = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			
			String hashedPassword = PasswordUtils.hashPassword(password);

			
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, hashedPassword);

			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()) {
				String nome = rs.getString(NOME);
				String cognome = rs.getString(COGNOME);
				return new GestoreOrdini(email, nome, cognome, hashedPassword);
			}

			rs.close();
			
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
		
		return null;
	}
	
	/*** MACRO ***/
	private static final String TABLE = "gestore_ordini";
	private static final String NOME = "nome";
	private static final String COGNOME = "cognome";

	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(GestoreOrdiniIDS.class.getName());
	private static final String ERROR = "Errore";


	
}
