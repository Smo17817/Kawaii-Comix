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

	private DataSource ds = null;
	private Connection connection = null;

	
	
	public ProdottoIDS(DataSource ds) {
		super();
		this.ds = ds;
		try {
			connection = ds.getConnection();
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
	}

	@Override
	public Boolean doSaveProdotto(Prodotto prodotto) throws SQLException {
		String query = "INSERT INTO " + ProdottoIDS.TABLE
				+ " (isbn, nome, autore, descrizione, immagine_prod, prezzo, quantita, genere_nome, categoria_nome ,copie_vendute) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ? , ?)";

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
			preparedStatement.setString(9 ,prodotto.getCategoria());
			preparedStatement.setInt(10 , prodotto.getCopieVendute());

			if(preparedStatement.executeUpdate() > 0)
				return  true;

		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}

		return  false;
	}

	@Override
	public Boolean doDeleteProdotto(String isbn) throws SQLException {
		String query = "DELETE FROM " + ProdottoIDS.TABLE + " WHERE isbn = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {

			preparedStatement.setString(1, isbn);

			if(preparedStatement.executeUpdate() > 0)
				return true;
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
		return false;
	}

	@Override
	public Boolean doUpdateProdotto(Prodotto prodotto) throws SQLException {
		String query = "UPDATE " + ProdottoIDS.TABLE
				+ " SET nome = ?, autore = ?, descrizione = ?, immagine_prod = ?, prezzo = ?, quantita = ?, categoria_nome = ?, genere_nome = ?"
				+ " WHERE isbn = ?";


		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			
			
			preparedStatement.setString(1, prodotto.getNome());
			preparedStatement.setString(2, prodotto.getAutore());
			preparedStatement.setString(3, prodotto.getDescrizione());
			preparedStatement.setString(4, prodotto.getImmagine());
			preparedStatement.setDouble(5, prodotto.getPrezzo());
			preparedStatement.setInt(6, prodotto.getQuantita());
			preparedStatement.setString(7, prodotto.getCategoria());
			preparedStatement.setString(8, prodotto.getGenere());
			preparedStatement.setString(9  ,prodotto.getIsbn());

			if(preparedStatement.executeUpdate() > 0)
				return true;
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}
		return false;
	}

	@Override
	public Collection<Prodotto> doRetreiveAllProdotti() throws SQLException {
		String query = "SELECT * FROM " + ProdottoIDS.TABLE;
		ArrayList<Prodotto> prodotti = new ArrayList<>();

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				String isbn = rs.getString(ISBN);
				String nome = rs.getString(NOME);
				String autore = rs.getString(AUTORE);
				String descrizione = rs.getString(DESCRIZIONE);
				String img = rs.getString(IMMAGINE);
				Double prezzo = rs.getDouble(PREZZO);
				Integer quantita = rs.getInt(QUANTITA);
				String genere = rs.getString(GENERE);
				String categoria = rs.getString(CATEGORIA);
				Integer copieVendute = rs.getInt(COPIE_VENDUTE);

				Prodotto prodotto = new Prodotto(isbn, nome, autore, descrizione, img, prezzo, quantita, genere,
						categoria, copieVendute);
				prodotti.add(prodotto);
			}
			rs.close();
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}

		return prodotti;
	}

	@Override
	public Prodotto doRetrieveByIsbn(String isbn) throws SQLException {
		String query = "SELECT * FROM " + ProdottoIDS.TABLE + " WHERE isbn = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setString(1, isbn);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				String nome = rs.getString(NOME);
				String autore = rs.getString(AUTORE);
				String descrizione = rs.getString(DESCRIZIONE);
				String img = rs.getString(IMMAGINE);
				Double prezzo = rs.getDouble(PREZZO);
				Integer quantita = rs.getInt(QUANTITA);
				String genere = rs.getString(GENERE);
				String categoria = rs.getString(CATEGORIA);
				Integer copieVendute = rs.getInt(COPIE_VENDUTE);

				return new Prodotto(isbn, nome, autore, descrizione, img, prezzo, quantita, genere, categoria,
						copieVendute);
			}

			rs.close();
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}

		return null;
	}
	
	@Override
	public Prodotto doRetrieveByNome(String nome) throws SQLException {
		String query = "SELECT * FROM " + ProdottoIDS.TABLE + " WHERE nome = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setString(1, nome);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				String isbn = rs.getString(ISBN);
				String autore = rs.getString(AUTORE);
				String descrizione = rs.getString(DESCRIZIONE);
				String img = rs.getString(IMMAGINE);
				Double prezzo = rs.getDouble(PREZZO);
				Integer quantita = rs.getInt(QUANTITA);
				String genere = rs.getString(GENERE);
				String categoria = rs.getString(CATEGORIA);
				Integer copieVendute = rs.getInt(COPIE_VENDUTE);


				return new Prodotto(isbn, nome, autore, descrizione, img, prezzo, quantita, genere, categoria,
						copieVendute);
			}

			rs.close();
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}

		return null;
	}

	@Override
	public Collection<Prodotto> lastSaved() throws SQLException {
		String query = "SELECT * FROM prodotti ORDER BY isbn DESC LIMIT 5";
		ArrayList<Prodotto> prodotti = new ArrayList<>();

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				String isbn = rs.getString(ISBN);
				String nome = rs.getString(NOME);
				String autore = rs.getString(AUTORE);
				String descrizione = rs.getString(DESCRIZIONE);
				String img = rs.getString(IMMAGINE);
				Double prezzo = rs.getDouble(PREZZO);
				Integer quantita = rs.getInt(QUANTITA);
				String genere = rs.getString(GENERE);
				String categoria = rs.getString(CATEGORIA);
				Integer copieVendute = rs.getInt(COPIE_VENDUTE);
				Prodotto prodotto = new Prodotto(isbn, nome, autore, descrizione, img, prezzo, quantita, genere,
						categoria, copieVendute);

				prodotti.add(prodotto);
			}

			rs.close();
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}

		return prodotti;
	}

	@Override
	public Boolean updateCopieVendute(Prodotto prodotto) {
		String query = "UPDATE " + ProdottoIDS.TABLE 
				+ " SET copie_vendute = ?"
				+ " WHERE isbn = ?";

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {

			preparedStatement.setInt(1, prodotto.getCopieVendute());
			preparedStatement.setString(2, prodotto.getIsbn());

			if(preparedStatement.executeUpdate() > 0)
				return true;
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}

		return  false;
	}

	@Override
	public Collection<Prodotto> bestSellers() throws SQLException {
		String query = "SELECT * FROM prodotti ORDER BY copie_vendute DESC LIMIT 5";
		ArrayList<Prodotto> prodotti = new ArrayList<>();

		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				String isbn = rs.getString(ISBN);
				String nome = rs.getString(NOME);
				String autore = rs.getString(AUTORE);
				String descrizione = rs.getString(DESCRIZIONE);
				String img = rs.getString(IMMAGINE);
				Double prezzo = rs.getDouble(PREZZO);
				Integer quantita = rs.getInt(QUANTITA);
				String genere = rs.getString(GENERE);
				String categoria = rs.getString(CATEGORIA);
				Integer copieVendute = rs.getInt(COPIE_VENDUTE);
				Prodotto prodotto = new Prodotto(isbn, nome, autore, descrizione, img, prezzo, quantita, genere,
						categoria, copieVendute);

				prodotti.add(prodotto);
			}

			rs.close();
		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
		}

		return prodotti;
	}

	@Override
	public ArrayList<String> doRetrieveAllProductsName(){
		String query = "SELECT nome FROM " + ProdottoIDS.TABLE + " ORDER BY nome";
		ArrayList<String>  nomiList = new ArrayList<>();

		try (Connection connection = ds.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query);) {

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()){
				nomiList.add(rs.getString(NOME));
			}

		} catch (SQLException e) {
			logger.log(Level.ALL, ERROR, e);
        }
		return  nomiList;
    }

	
	/*** MACRO ***/
	private static final String TABLE = "prodotti";
	private static final String ISBN = "isbn";
	private static final String NOME = "nome";
	private static final String AUTORE = "autore";
	private static final String DESCRIZIONE = "descrizione";
	private static final String IMMAGINE = "immagine_prod";
	private static final String PREZZO = "prezzo";
	private static final String QUANTITA = "quantita";
	private static final String GENERE = "genere_nome";
	private static final String CATEGORIA = "categoria_nome";
	private static final String COPIE_VENDUTE = "copie_vendute";
	
	/*** LOGGER ***/
	private static final Logger logger = Logger.getLogger(ProdottoIDS.class.getName());
	private static final String ERROR = "Errore";

}
