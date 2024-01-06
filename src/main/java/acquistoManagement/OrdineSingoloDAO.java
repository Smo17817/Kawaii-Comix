package acquistoManagement;

import java.sql.SQLException;
import java.util.Collection;

import catalogoManagement.Prodotto;

public interface OrdineSingoloDAO {
	public void doSaveOrdineSingolo(OrdineSingolo ordineSingolo) throws SQLException;

	public Boolean doDeleteOrdineSingolo(Integer id) throws SQLException;

	public Boolean doUpdateOrdineSingolo(Integer id, Integer quantita, Double totParziale, Integer ordineId, Prodotto prodotto)
			throws SQLException;

	public Collection<OrdineSingolo> doRetrieveAllOrdineSingolo() throws SQLException;

	public Collection<OrdineSingolo> doRetrieveAllByOrdineId(Integer ordineId) throws SQLException;
	
	public OrdineSingolo doRetrieveById(Integer id) throws SQLException;
}
