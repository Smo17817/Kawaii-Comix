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

	public UserIDS(DataSource ds) {
		super();
		this.ds = ds;
	}

	@Override
	public void doSaveUser(User user) throws SQLException {
		
		String query = "INSERT INTO " + UserIDS.TABLE
				+ " (email_address, password, nome, cognome, indirizzo, città ,comune, codice_postale, provincia, nazione) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {

			preparedStatement.setString(1, user.getEmail());
			preparedStatement.setString(2, user.getPassword());
			preparedStatement.setString(3, user.getNome());
			preparedStatement.setString(4, user.getCognome());
			preparedStatement.setString(5, user.getIndirizzo());
			preparedStatement.setString(6, user.getCittà());
			preparedStatement.setString(7, user.getComune());
			preparedStatement.setString(8, user.getCap());
			preparedStatement.setString(9, user.getProvincia());
			preparedStatement.setString(10, user.getNazione());

			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			logger.log(Level.ALL, error, e);
		}

	}

	@Override
	public Boolean doDeleteUser(String id) throws SQLException {
		String query = "DELETE FROM " + UserIDS.TABLE + " WHERE id = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {

			preparedStatement.setString(1, id);

			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			logger.log(Level.ALL, error, e);
		}
		return false;
	}

	@Override
	public Boolean doUpdateUser(User user) throws SQLException {
		String query = "UPDATE " + UserIDS.TABLE
				+ "SET email_address = ?, password = ?, nome = ?, cognome = ?, indirizzo = ?, città = ?,  comune = ?, codice_postale = ?, provincia = ?, nazione = ? "
				+ "WHERE id = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {

			preparedStatement.setString(1, user.getEmail());
			preparedStatement.setString(2, user.getPassword());
			preparedStatement.setString(3, user.getNome());
			preparedStatement.setString(4, user.getCognome());
			preparedStatement.setString(5, user.getIndirizzo());
			preparedStatement.setString(6, user.getCittà());
			preparedStatement.setString(7, user.getComune());
			preparedStatement.setString(8, user.getCap());
			preparedStatement.setString(9, user.getProvincia());
			preparedStatement.setString(10, user.getNazione());
			preparedStatement.setInt(11, user.getId());

			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			logger.log(Level.ALL, error, e);
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
				String città = rs.getString(CITTA);
				String comune = rs.getString(COMUNE);
				String cap = rs.getString(CAP);
				String provincia = rs.getString(PROVINCIA);
				String nazione = rs.getString(NAZIONE);

				User user = new User(id, email, password, nome, cognome, indirizzo, città, comune, cap, provincia, nazione);

				users.add(user);
			}

			rs.close();

			return users;
		} catch (SQLException e) {
			logger.log(Level.ALL, error, e);
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
				String città = rs.getString(CITTA);
				String comune = rs.getString(COMUNE);
				String cap = rs.getString(CAP);
				String provincia = rs.getString(PROVINCIA);
				String nazione = rs.getString(NAZIONE);

				return new User(id, email, password, nome, cognome, indirizzo, città, comune, cap, provincia, nazione);
			}

			rs.close();

		} catch (SQLException e) {
			logger.log(Level.ALL, error, e);
		}

		return null;
	}

	@Override
	public User doRetrieveUser(String email, String password) throws SQLException {
		String query = "SELECT * FROM " + UserIDS.TABLE + " WHERE email_address = ? and password = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);

			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {

				Integer id = rs.getInt(ID);
				String nome = rs.getString(NOME);
				String cognome = rs.getString(COGNOME);
				String indirizzo = rs.getString(INDIRIZZO);
				String città = rs.getString(CITTA);
				String comune = rs.getString(COMUNE);
				String cap = rs.getString(CAP);
				String provincia = rs.getString(PROVINCIA);
				String nazione = rs.getString(NAZIONE);

				return new User(id, email, password, nome, cognome, indirizzo, città, comune, cap, provincia, nazione);
			}

			rs.close();

		} catch (SQLException e) {
			logger.log(Level.ALL, error, e);
		}

		return null;
	}

	@Override
	public boolean EmailExists(String email) throws SQLException {

		String query = "SELECT * FROM site_user WHERE email_address = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {

			preparedStatement.setString(1, email);
			ResultSet resultSet = preparedStatement.executeQuery();
			return resultSet.next();

		} catch (SQLException e) {
			logger.log(Level.ALL, error, e);
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
	private static final String CITTA = "città";
	private static final String COMUNE = "comune";
	private static final String CAP = "codice_postale";
	private static final String PROVINCIA = "provincia";
	private static final String NAZIONE = "nazione";

	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(UserIDS.class.getName());
	private static final String error = "Errore";

}
