package utenteManagement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class UserIDS implements UserDAO {
 
	private DataSource ds = null;
	
	private Connection connection = null;

	public UserIDS(DataSource ds) {
		super();
		this.ds = ds;
		try {
			connection = ds.getConnection();
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
	}
	

	@Override
	public boolean doSaveUser(User user) throws SQLException {	
		
		String query = "INSERT INTO " + UserIDS.TABLE
				+ " (email_address, password, nome, cognome, indirizzo, citta, codice_postale, provincia, nazione) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			String hashedPassword = PasswordUtils.hashPassword(user.getPassword());

			preparedStatement.setString(1, user.getEmail());
			preparedStatement.setString(2, hashedPassword);
			preparedStatement.setString(3, user.getNome());
			preparedStatement.setString(4, user.getCognome());
			preparedStatement.setString(5, user.getIndirizzo());
			preparedStatement.setString(6, user.getCitta());
			preparedStatement.setString(7, user.getCap());
			preparedStatement.setString(8, user.getProvincia());
			preparedStatement.setString(9, user.getNazione());

			if((preparedStatement.executeUpdate() > 0))
				return  true;


		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
		return  false;

	}


	@Override
	public Boolean doDeleteUser(Integer id) throws SQLException {
		String query = "DELETE FROM " + UserIDS.TABLE + " WHERE id = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {

			preparedStatement.setInt(1, id);

			if(preparedStatement.executeUpdate() > 0)
				return true;
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
		return false;
	}

	@Override
	public Boolean doUpdateUser(User user) throws SQLException {
		String query = "UPDATE " + UserIDS.TABLE
				+ " SET email_address = ?, password = ?, nome = ?, cognome = ?, indirizzo = ?, citta = ?, codice_postale = ?, provincia = ?, nazione = ? "
				+ " WHERE id = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			
			String hashedPassword = PasswordUtils.hashPassword(user.getPassword());
			
			preparedStatement.setString(1, user.getEmail());
			preparedStatement.setString(2, hashedPassword);
			preparedStatement.setString(3, user.getNome());
			preparedStatement.setString(4, user.getCognome());
			preparedStatement.setString(5, user.getIndirizzo());
			preparedStatement.setString(6, user.getCitta());
			preparedStatement.setString(7, user.getCap());
			preparedStatement.setString(8, user.getProvincia());
			preparedStatement.setString(9, user.getNazione());
			preparedStatement.setInt(10, user.getId());

			if(preparedStatement.executeUpdate() > 0)
				return true;
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}

		return false;
	}

	@Override
	public Collection<User> doRetrieveAllUsers() throws SQLException {
		String query = "SELECT * FROM " + UserIDS.TABLE;
		ArrayList<User> users = new ArrayList<>();

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Integer id = rs.getInt(ID);
				String email = rs.getString(EMAIL);
				String password = rs.getString(PASSWORD);
				String nome = rs.getString(NOME);
				String cognome = rs.getString(COGNOME);
				String indirizzo = rs.getString(INDIRIZZO);
				String citta = rs.getString(CITTA);
				String cap = rs.getString(CAP);
				String provincia = rs.getString(PROVINCIA);
				String nazione = rs.getString(NAZIONE);

				User user = new User(id, email, password, nome, cognome, indirizzo, citta, cap, provincia, nazione);

				users.add(user);
			}

			rs.close();

			return users;
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}

		return users;
	}

	@Override
	public User doRetrieveById(Integer id) throws SQLException {
		String query = "SELECT * FROM " + UserIDS.TABLE + " WHERE id = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				String email = rs.getString(EMAIL);
				String password = rs.getString(PASSWORD);
				String nome = rs.getString(NOME);
				String cognome = rs.getString(COGNOME);
				String indirizzo = rs.getString(INDIRIZZO);
				String citta = rs.getString(CITTA);
				String cap = rs.getString(CAP);
				String provincia = rs.getString(PROVINCIA);
				String nazione = rs.getString(NAZIONE);

				return new User(id, email, password, nome, cognome, indirizzo, citta, cap, provincia, nazione);
			}

			rs.close();

		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}

		return null;
	}

	@Override
	public User doRetrieveUser(String email, String password) throws SQLException {
		String query = "SELECT * FROM " + UserIDS.TABLE + " WHERE email_address = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setString(1, email);

			ResultSet rs = preparedStatement.executeQuery();
			
			//Se esisteun utente con quella mail e il cui hash della password corriposnde...
			if (rs.next() && PasswordUtils.verifyPassword(password, rs.getString(PASSWORD))) {
				Integer id = rs.getInt(ID);
				String nome = rs.getString(NOME);
				String cognome = rs.getString(COGNOME);
				String indirizzo = rs.getString(INDIRIZZO);
				String citta = rs.getString(CITTA);
				String cap = rs.getString(CAP);
				String provincia = rs.getString(PROVINCIA);
				String nazione = rs.getString(NAZIONE);

				return new User(id, email, rs.getString(PASSWORD), nome, cognome, indirizzo, citta, cap, provincia, nazione);
			}

			rs.close();

		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}

		return null;
	}

	@Override
	public Boolean doUpdateUserPassword(String mail, String password){
		String query = "UPDATE " + UserIDS.TABLE + " SET password = ? WHERE email_address = ?";

		String hashedPassword = PasswordUtils.hashPassword(password);
		try (Connection connection = ds.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query);) {

			preparedStatement.setString(1, hashedPassword);
			preparedStatement.setString(2, mail);

			if(preparedStatement.executeUpdate() == 1){
				return  true;
			}

		} catch (SQLException e) {
            throw new RuntimeException(e);
        }

		return false;
    }

	@Override
	public boolean emailExists(String email) throws SQLException {

		String query = "SELECT * FROM site_user WHERE email_address = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {

			preparedStatement.setString(1, email);
			ResultSet resultSet = preparedStatement.executeQuery();
			return resultSet.next();

		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}

		return false;

	}
	

	/*** MACRO ***/
	private static final String TABLE = "site_user";
	private static final String ID = "id";
	private static final String EMAIL = "email_address";
	private static final String PASSWORD = "password";
	private static final String NOME = "nome";
	private static final String COGNOME = "cognome";
	private static final String INDIRIZZO = "indirizzo";
	private static final String CITTA = "citta";
	private static final String CAP = "codice_postale";
	private static final String PROVINCIA = "provincia";
	private static final String NAZIONE = "nazione";

	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(UserIDS.class.getName());
	private static final String ERROR = "Errore";

}
