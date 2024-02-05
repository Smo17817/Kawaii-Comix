package acquistoManagement;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;

public interface OrdineDAO {
	public Boolean doSaveOrdine(Ordine ordine) throws SQLException;

	public Boolean doDeleteOrdine(Integer id) throws SQLException;

	public Boolean doUpdateOrdine(Ordine ordine) throws SQLException;
	
	public Boolean doUpdateStatoById(Integer id, Integer stato) throws SQLException;;

	public Collection<Ordine> doRetrieveAllOrdini() throws SQLException;

	public Ordine doRetrieveById(Integer id) throws SQLException;

	public Collection<Ordine> doRetrieveByUserId(Integer id) throws SQLException;
	
	public Collection<OrdineSingolo> doRetrieveAllOrdiniSingoli(Ordine ordine) throws SQLException;
	
	public void doSaveOrdineSingoloAssociato(OrdineSingolo ordineSingolo) throws SQLException;
}
