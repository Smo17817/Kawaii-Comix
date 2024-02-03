package catalogoManagement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public interface ProdottoDAO {
	public Boolean doSaveProdotto(Prodotto prodotto) throws SQLException;

	public Boolean doDeleteProdotto(String isbn) throws SQLException;

	public Boolean doUpdateProdotto(Prodotto prodotto) throws SQLException;

	public Collection<Prodotto> doRetreiveAllProdotti() throws SQLException;

	public Prodotto doRetrieveByIsbn(String isbn) throws SQLException;
	
	public Prodotto doRetrieveByNome(String nome) throws SQLException;
	//Restituisce gli ultimi cinque prodotti aggiunti
	public Collection<Prodotto> lastSaved() throws SQLException;

	public ArrayList<String> doRetrieveAllProductsName();
	//Restituisce le copie aggiornate
	public Boolean updateCopieVendute(Prodotto prodotto) throws  SQLException;
	//Restituisce i prodotti più venduti
	public Collection<Prodotto> bestSellers() throws SQLException;
}
