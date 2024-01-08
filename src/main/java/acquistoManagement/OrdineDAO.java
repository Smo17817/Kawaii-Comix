package acquistoManagement;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;

public interface OrdineDAO {
	public void doSaveOrdine(Ordine ordine, OrdineSingolo... ordiniSingoli) throws SQLException;

	public Boolean doDeleteOrdine(Integer id) throws SQLException;

	public Boolean doUpdateOrdine(Integer id, Date data, Double totale, Integer userId, Integer stato,
			Integer metodoSpedizione) throws SQLException;

	public Collection<Ordine> doRetrieveAllOrdini() throws SQLException;

	public Collection<Ordine> doRetrieveById(Integer id) throws SQLException;
	
	public Collection<OrdineSingolo> doRetrieveAllOrdiniSingoli(Ordine ordine) throws SQLException;
}
