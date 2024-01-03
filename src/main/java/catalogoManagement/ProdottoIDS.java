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

public class ProdottoIDS implements ProdottoDAO {
	private static final Logger logger = Logger.getLogger(ProdottoIDS.class.getName());
	private static final String error = "Errore";

	private static final String TABLE = "prodotti";
	private DataSource ds = null;

	public ProdottoIDS(DataSource ds) {
		super();
		this.ds = ds;
	}

	@Override
	public void doSaveProdotto(Prodotto prodotto) throws SQLException {
		String query = "INSERT INTO " + ProdottoIDS.TABLE
				+ " (isbn, nome, autore, descrizione, immagine_prod, prezzo, quantita, genere_nome, categoria_nome) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {

			preparedStatement.setString(1, prodotto.getIsbn());
			preparedStatement.setString(2, prodotto.getNome());
			preparedStatement.setString(3, prodotto.getAutore());
			preparedStatement.setString(4, prodotto.getDescrizione());
			preparedStatement.setString(5, prodotto.getImmagine());
			preparedStatement.setDouble(6, prodotto.getPrezzo());
			preparedStatement.setInt(7, prodotto.getQuantita());
			preparedStatement.setString(8, prodotto.getGenere());
			preparedStatement.setString(9, prodotto.getCategoria());

		} catch (SQLException e) {
			logger.log(Level.ALL, error, e);
		}

	}

	public Boolean doDeleteProdotto(String isbn) throws SQLException {
		String query = "DELETE FROM " + ProdottoIDS.TABLE + " WHERE isbn = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {

			preparedStatement.setString(1, isbn);

			return true;
		} catch (SQLException e) {
			logger.log(Level.ALL, error, e);
		}
		return false;
	}

	public void doUpdateProdotto(Prodotto prodotto) throws SQLException {
		String query = "UPDATE " + ProdottoIDS.TABLE
				+ "SET nome = ?, autore = ?, descrizione = ?, immagine_prod = ?, prezzo = ?, quantita = ?, categoria_nome = ?, genere_nome = ? "
				+ "WHERE isbn = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {

			preparedStatement.setString(1, prodotto.getNome());
			preparedStatement.setString(2, prodotto.getAutore());
			preparedStatement.setString(3, prodotto.getDescrizione());
			preparedStatement.setString(4, prodotto.getImmagine());
			preparedStatement.setDouble(5, prodotto.getPrezzo());
			preparedStatement.setInt(6, prodotto.getQuantita());
			preparedStatement.setString(7, prodotto.getGenere());
			preparedStatement.setString(8, prodotto.getCategoria());
			preparedStatement.setString(9, prodotto.getIsbn());

		} catch (SQLException e) {
			logger.log(Level.ALL, error, e);
		}

	}

	public Collection<Prodotto> doRetreiveAllProdotti() throws SQLException {
		String query = "SELECT * FROM " + ProdottoIDS.TABLE;
		ArrayList<Prodotto> prodotti = new ArrayList<>();

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				String isbn = rs.getString("isbn");
				String nome = rs.getString("nome");
				String autore = rs.getString("autore");
				String descrizione = rs.getString("descrizione");
				String img = rs.getString("immagine_prod");
				Double prezzo = rs.getDouble("prezzo");
				Integer quantita = rs.getInt("quantita");
				String genere = rs.getString("genere_nome");
				String categoria = rs.getString("categoria_nome");

				Prodotto prodotto = new Prodotto(isbn, nome, autore, descrizione, img, prezzo, quantita, genere,
						categoria);
				prodotti.add(prodotto);
			}

			return prodotti;

		} catch (SQLException e) {
			logger.log(Level.ALL, error, e);
		}

		return prodotti;
	}

	public Prodotto doRetrieveByIsbn(String isbn) throws SQLException {
		String query = "SELECT * FROM " + ProdottoIDS.TABLE + " WHERE isbn = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setString(1, isbn);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {

				String nome = rs.getString("nome");
				String autore = rs.getString("autore");
				String descrizione = rs.getString("descrizione");
				String img = rs.getString("immagine_prod");
				Double prezzo = rs.getDouble("prezzo");
				Integer quantita = rs.getInt("quantita");
				String genere = rs.getString("genere_nome");
				String categoria = rs.getString("categoria_nome");

				return new Prodotto(isbn, nome, autore, descrizione, img, prezzo, quantita, genere, categoria);
			}

		} catch (SQLException e) {
			logger.log(Level.ALL, error, e);
		}
		
		return null;
	}

}
