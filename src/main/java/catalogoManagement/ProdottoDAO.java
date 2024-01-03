package catalogoManagement;

import java.sql.SQLException;
import java.util.Collection;

public interface ProdottoDAO {
	public void doSaveProdotto(Prodotto prodotto) throws SQLException;

	public Boolean doDeleteProdotto(String isbn) throws SQLException;

	public void doUpdateProdotto(Prodotto prodotto) throws SQLException;

	public Collection<Prodotto> doRetreiveAllProdotti() throws SQLException;

	public Prodotto doRetrieveByIsbn(String isbn) throws SQLException;
}
