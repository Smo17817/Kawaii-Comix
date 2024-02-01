package acquistoManagement;

import catalogoManagement.Prodotto;
import catalogoManagement.ProdottoIDS;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CarrelloIDS implements CarrelloDAO {

	private DataSource ds = null;
	private Connection connection = null;
	public CarrelloIDS(DataSource ds) {
		super();
		this.ds = ds;
		try {
			connection = ds.getConnection();
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
	}

	@Override
	public void doCreateCarrello(int userId) {
		String query = "INSERT INTO " + CarrelloIDS.TABLE + " (user_id) VALUES (?)";
		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, userId);

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
	}

	@Override
	public Boolean doDeleteCarrello(int carrelloId) {
		String query = "DELETE FROM " + CarrelloIDS.TABLE + " WHERE id = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {

			preparedStatement.setInt(1, carrelloId);

			if (preparedStatement.executeUpdate() > 0) // controllo se le righe sono state modificate
				return true;

		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
		return false;
	}

	@Override
	public void doSvuotaCarrello(Carrello carrello) {
		String query = "INSERT INTO " + CarrelloIDS.TABLE2 + " (carrello_id , prodotto_isbn) VALUES (?,?)";
		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			HashSet<Prodotto> listaProdotti = (HashSet<Prodotto>) carrello.getListaProdotti();
			ArrayList<String> isbnList = new ArrayList<>();

			for (Prodotto prodotto : listaProdotti) {
				isbnList.add(prodotto.getIsbn());
			}

			for (String isbn : isbnList) {
				preparedStatement.setInt(1, carrello.getCarrelloId());
				preparedStatement.setString(2, isbn);
				preparedStatement.executeUpdate();
			}

			carrello.empty();
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}

	}

	@Override
	public Carrello doRetrieveCarrello(int userId) {
		String query = "SELECT * FROM " + CarrelloIDS.TABLE + " WHERE user_id = ?";
		Carrello carrello = new Carrello();
		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, userId);

			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) { // se esiste già un carrello per l'utente allora lo recupero dal DB
				int carrelloid = rs.getInt("id");
				carrello = new Carrello(carrelloid);
			} else { // altrimenti se è il primo login creo un nuovo carrello per l'utente e poi
						// glielo assegno
				this.doCreateCarrello(userId);

				rs = preparedStatement.executeQuery();
				carrello = new Carrello(rs.getInt("id"));
			}
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}

		return carrello;
	}

	@Override
	public void doDeleteProdottiCarrello(Carrello carrello){
		HashSet<Prodotto> prodotti = (HashSet<Prodotto>) carrello.getListaProdotti();
		ArrayList<String> isbnList = new ArrayList<>();

		for(Prodotto prodotto : prodotti){
			isbnList.add(prodotto.getIsbn());
		}

		String query = "DELETE FROM " + CarrelloIDS.TABLE2 + " WHERE prodotto_isbn = ?";

		try (Connection connection = ds.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			for (String isbn : isbnList) {
				preparedStatement.setString(1 , isbn);
				preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
        }
    }

	@Override
	public Carrello doRetrieveProdottiCarrello(Carrello carrello) {
		String query = "SELECT * FROM " + CarrelloIDS.TABLE2 + " WHERE carrello_id = ?";
		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, carrello.getCarrelloId());

			ResultSet rs = preparedStatement.executeQuery();

			HashSet<Prodotto> prodottiCarrello = (HashSet<Prodotto>) carrello.getListaProdotti();
			ArrayList<String> isbnList = new ArrayList<>();
			while (rs.next()) {
				isbnList.add(rs.getString(("prodotto_isbn")));
			}

			ProdottoIDS prodottoIDS = new ProdottoIDS(ds);
			Collection<Prodotto> allProducts = prodottoIDS.doRetreiveAllProdotti();

			for (Prodotto prodotto : allProducts) {
				if (isbnList.contains(prodotto.getIsbn())) {
					String nomeprod = prodotto.getNome();
					String autore = prodotto.getAutore();
					String descrizione = prodotto.getDescrizione();
					String img = prodotto.getImmagine();
					String genere = prodotto.getGenere();
					String categoria = prodotto.getCategoria();
					Integer quantita = prodotto.getQuantita();
					Double prezzo = prodotto.getPrezzo();
					Integer copieVendute = prodotto.getCopieVendute();
					Prodotto prodotto1 = new Prodotto(prodotto.getIsbn(), nomeprod, autore, descrizione, img, prezzo,
							quantita, genere, categoria, copieVendute);
					prodottiCarrello.add(prodotto1);
				}
			}

			for (String isbn : isbnList) {
				query = "DELETE FROM " + CarrelloIDS.TABLE2 + " WHERE prodotto_isbn = ?";
				connection.prepareStatement(query);

				preparedStatement.setString(1, isbn);

				preparedStatement.executeUpdate();
			}

			carrello.setListaProdotti(prodottiCarrello);

		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}

		return carrello;
	}
	
	/*** MACRO ***/

	private static final String TABLE = "carrello";
	private static final String TABLE2 = "carrello_prodotto";
	
	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(CarrelloIDS.class.getName());
	private static final String ERROR = "Errore";
}
