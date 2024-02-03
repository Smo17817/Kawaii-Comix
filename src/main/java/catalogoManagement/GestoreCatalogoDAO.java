package catalogoManagement;

import java.sql.SQLException;

public interface GestoreCatalogoDAO {
	public Boolean doSaveGestore(GestoreCatalogo gestoreCatalogo) throws SQLException;

	public Boolean doUpdateGestore(GestoreCatalogo gestoreOrdini) throws SQLException;

	public Boolean doDeleteGestore(String email) throws SQLException;
	
	public GestoreCatalogo doRetrieveByAuthentication(String email, String password) throws SQLException;
}
