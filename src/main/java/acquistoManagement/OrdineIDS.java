package acquistoManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class OrdineIDS implements OrdineDAO {

	private DataSource ds = null;
	private Connection connection = null;

	private  OrdineSingoloIDS ordineSingoloIDS;
	public OrdineIDS(DataSource ds) {
		super();
		this.ds = ds;
		this.ordineSingoloIDS = new OrdineSingoloIDS(ds);
		try {
			connection = ds.getConnection();
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
	}

	@Override
	public Boolean doSaveOrdine(Ordine ordine) throws SQLException {
		String query = "INSERT INTO " + OrdineIDS.TABLE
				+ " (id ,data, totale, site_user_id, stato_ordine_id, metodo_spedizione_id) VALUES (?,?, ?, ?, ?, ?)";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {


			java.sql.Date sqlDate = new java.sql.Date(ordine.getJavaDate().getTime());

			preparedStatement.setInt(1 , ordine.getId());
			preparedStatement.setDate(2, sqlDate);
			preparedStatement.setDouble(3, ordine.getTotale());
			preparedStatement.setInt(4, ordine.getUserId());
			preparedStatement.setInt(5, ordine.getStato());
			preparedStatement.setInt(6, ordine.getMetodoSpedizione());

			if(preparedStatement.executeUpdate()>0) {

			// Salva tutti i singoli ordini associati all'ordine cumulativo
			for (OrdineSingolo ordineSingolo : ordine.getOrdiniSingoli())
				doSaveOrdineSingoloAssociato(ordineSingolo);
			return true;
			}
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
		return false;
	}

	@Override
	public Boolean doDeleteOrdine(Integer id) throws SQLException {
		String query = "DELETE FROM " + OrdineIDS.TABLE + " WHERE id = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {

			preparedStatement.setInt(1, id);

			// controllo se le righe sono state modificate
			if (preparedStatement.executeUpdate() > 0)
				return true;

		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
		return false;
	}

	@Override
	public Boolean doUpdateOrdine(Ordine ordine) throws SQLException {
		String query = "UPDATE " + OrdineIDS.TABLE
				+ "SET data = ?, totale = ?, site_user_id = ?, stato_ordine_id = ?, metodo_spedizione_id = ? "
				+ "WHERE id = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			java.sql.Date sqlDate = new java.sql.Date(ordine.getJavaDate().getTime());

			preparedStatement.setDate(1, sqlDate);
			preparedStatement.setDouble(2, ordine.getTotale());
			preparedStatement.setInt(3, ordine.getUserId());
			preparedStatement.setInt(4, ordine.getStato());
			preparedStatement.setInt(5, ordine.getMetodoSpedizione());
			preparedStatement.setInt(6, ordine.getId());

			if (preparedStatement.executeUpdate() > 0)
				return true;
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
		return false;
	}
	
	@Override
	public Boolean doUpdateStatoById(Integer id, Integer stato) throws SQLException {
		String query = "UPDATE " + OrdineIDS.TABLE
				+ " SET stato_ordine_id = ? "
				+ " WHERE id = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {

			preparedStatement.setInt(1, stato);
			preparedStatement.setInt(2, id);

			if (preparedStatement.executeUpdate() > 0)
				return true;
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
		return false;
	}

	@Override
	public Collection<Ordine> doRetrieveAllOrdini() throws SQLException {
		String query = "SELECT * FROM " + OrdineIDS.TABLE;
		ArrayList<Ordine> ordini = new ArrayList<>();

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Integer id = rs.getInt(ID);
				Date data = new Date(rs.getDate(DATA).getTime());
				Double totale = rs.getDouble(TOTALE);
				Integer userId = rs.getInt(USER_ID);
				Integer stato = rs.getInt(STATO);
				Integer metodoSpedizione = rs.getInt(METODO_SPEDIZIONE);

				Ordine ordine = new Ordine(id, data, totale, userId, stato, metodoSpedizione);
				// Aggiunge la lista dei singoli ordini associati
				ordine.setOrdiniSingoli((new ArrayList<>(doRetrieveAllOrdiniSingoli(ordine))));

				ordini.add(ordine);
			}

			rs.close();

			return ordini;
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}

		return ordini;
	}

	@Override
	public Ordine doRetrieveById(Integer id) throws SQLException {
		String query = "SELECT * FROM " + OrdineIDS.TABLE + " WHERE id= ?";


		try (Connection connection = ds.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1,  id);
			ResultSet rs = preparedStatement.executeQuery();

			if(rs.next()) {
				Date data = new Date(rs.getDate(DATA).getTime());
				Double totale = rs.getDouble(TOTALE);
				Integer userId = rs.getInt(USER_ID);
				Integer stato = rs.getInt(STATO);
				Integer metodoSpedizione = rs.getInt(METODO_SPEDIZIONE);

				Ordine ordine = new Ordine(id, data, totale, userId, stato, metodoSpedizione);


				// Aggiunge la lista dei singoli ordini associati
				return ordine;
			}
			rs.close();
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}

		return null;
	}
	@Override
	public Collection<Ordine> doRetrieveByUserId(Integer userid) throws SQLException {
		String query = "SELECT * FROM " + OrdineIDS.TABLE + " WHERE site_user_id= ?";
		ArrayList<Ordine> ordini = new ArrayList<>();


		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1,  userid);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Integer id = rs.getInt(ID);
				Date data = new Date(rs.getDate(DATA).getTime());
				Double totale = rs.getDouble(TOTALE);
				Integer userId = rs.getInt(USER_ID);
				Integer stato = rs.getInt(STATO);
				Integer metodoSpedizione = rs.getInt(METODO_SPEDIZIONE);
				
				Ordine ordine = new Ordine(id, data, totale, userId, stato, metodoSpedizione);
				// Aggiunge la lista dei singoli ordini associati
				ordine.setOrdiniSingoli((new ArrayList<>(ordineSingoloIDS.doRetrieveAllByOrdineId(ordine.getId()))));

				ordini.add(ordine) ;
			}
			rs.close();

		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}

		return ordini;
	}
	
	@Override
	public void doSaveOrdineSingoloAssociato(OrdineSingolo ordineSingolo) throws SQLException {
		ordineSingoloIDS.doSaveOrdineSingolo(ordineSingolo);	
	}
	
	@Override
	public Collection<OrdineSingolo> doRetrieveAllOrdiniSingoli(Ordine ordine) throws SQLException {
		return ordineSingoloIDS.doRetrieveAllByOrdineId(ordine.getId());
	}

	/*** MACRO ***/
	private static final String TABLE = "ordini";
	private static final String ID = "id";
	private static final String DATA = "data";
	private static final String TOTALE = "totale";
	private static final String USER_ID = "site_user_id";
	private static final String STATO = "stato_ordine_id";
	private static final String METODO_SPEDIZIONE = "metodo_spedizione_id";

	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(OrdineIDS.class.getName());
	private static final String ERROR = "Errore";
}
