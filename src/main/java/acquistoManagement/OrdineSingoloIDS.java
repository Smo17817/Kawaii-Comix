package acquistoManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import catalogoManagement.Prodotto;
import catalogoManagement.ProdottoIDS;

public class OrdineSingoloIDS implements OrdineSingoloDAO {

	private DataSource ds = null;
	private Connection connection = null;

	public OrdineSingoloIDS(DataSource ds) {
		super();
		this.ds = ds;
		try {
			connection = ds.getConnection();
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
	}

	@Override
	public Boolean doSaveOrdineSingolo(OrdineSingolo ordineSingolo) throws SQLException {

		String query = "INSERT INTO " + OrdineSingoloIDS.TABLE
				+ " (quantità, totale_parziale, ordini_id, prodotti_isbn) VALUES (?, ?, ?, ?)";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setInt(1, ordineSingolo.getQuantita());
			preparedStatement.setDouble(2, ordineSingolo.getTotParziale());
			preparedStatement.setInt(3, ordineSingolo.getOrdineId());
			preparedStatement.setString(4, ordineSingolo.getProdotto().getIsbn());

			if(preparedStatement.executeUpdate()>0)
				return true;
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
		return false;

	}

	@Override
	public Boolean doDeleteOrdineSingolo(Integer id) throws SQLException {
		String query = "DELETE FROM " + OrdineSingoloIDS.TABLE + " WHERE id = ?";

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
	public Boolean doUpdateOrdineSingolo(Integer id, Integer quantita, Double totParziale, Integer ordineId,
			Prodotto prodotto) throws SQLException {
		String query = "UPDATE " + OrdineSingoloIDS.TABLE
				+ "SET quantità = ?, totale_parziale = ?, ordini_id = ?, prodotti_isbn = ? " + "WHERE id = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {

			preparedStatement.setInt(1, quantita);
			preparedStatement.setDouble(2, totParziale);
			preparedStatement.setInt(3, ordineId);
			preparedStatement.setString(4, prodotto.getIsbn());
			preparedStatement.setInt(5, id);

			if (preparedStatement.executeUpdate() > 0)
				return true;
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
		return false;
	}

	@Override
	public Collection<OrdineSingolo> doRetrieveAllOrdineSingolo() throws SQLException {
		String query = "SELECT * FROM " + OrdineSingoloIDS.TABLE;
		ArrayList<OrdineSingolo> ordiniSingoli = new ArrayList<>();
		ProdottoIDS prodottoIDS = new ProdottoIDS(ds);

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Integer id = rs.getInt(ID);
				Integer quantita = rs.getInt(QUANTITA);
				Double totParziale = rs.getDouble(TOT_PARZIALE);
				Integer ordineId = rs.getInt(ORDINE_ID);
				Prodotto prodotto = prodottoIDS.doRetrieveByIsbn(rs.getString(PRODOTTO_ISBN));

				ordiniSingoli.add(new OrdineSingolo(id, quantita, totParziale, ordineId, prodotto));
			}

			rs.close();

			return ordiniSingoli;
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}

		return ordiniSingoli;
	}

	@Override
	public Collection<OrdineSingolo> doRetrieveAllByOrdineId(Integer ordineId) throws SQLException {
		String query = "SELECT * FROM " + OrdineSingoloIDS.TABLE + " WHERE ordini_id = ?";

		ArrayList<OrdineSingolo> ordiniSingoli = new ArrayList<>();
		ProdottoIDS prodottoIDS = new ProdottoIDS(ds);

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, ordineId);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Integer id = rs.getInt(ID);
				Integer quantita = rs.getInt(QUANTITA);
				Double totParziale = rs.getDouble(TOT_PARZIALE);
				Prodotto prodotto = prodottoIDS.doRetrieveByIsbn(rs.getString(PRODOTTO_ISBN));

				ordiniSingoli.add(new OrdineSingolo(id, quantita, totParziale, ordineId, prodotto));
			}

			rs.close();

			return ordiniSingoli;
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}

		return ordiniSingoli;
	}

	@Override
	public OrdineSingolo doRetrieveById(Integer id) throws SQLException {
		String query = "SELECT * FROM " + OrdineSingoloIDS.TABLE + " WHERE id = ?";
		ProdottoIDS prodottoIDS = new ProdottoIDS(ds);
		

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				Integer quantita = rs.getInt(QUANTITA);
				Double totParziale = rs.getDouble(TOT_PARZIALE);
				Integer ordineId = rs.getInt(ORDINE_ID);
				Prodotto prodotto = prodottoIDS.doRetrieveByIsbn(rs.getString(PRODOTTO_ISBN));
				
				return new OrdineSingolo(id, quantita, totParziale, ordineId, prodotto);
			}

			rs.close();

		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}

		return null;
	}

	/*** MACRO ***/
	private static final String TABLE = "ordine_singolo";
	private static final String ID = "id";
	private static final String QUANTITA = "quantità";
	private static final String TOT_PARZIALE = "totale_parziale";
	private static final String ORDINE_ID = "ordini_id";
	private static final String PRODOTTO_ISBN = "prodotti_isbn";
	
	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(OrdineSingoloIDS.class.getName());
	private static final String ERROR = "Errore";
}
