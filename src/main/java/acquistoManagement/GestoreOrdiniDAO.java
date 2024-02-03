package acquistoManagement;

import java.sql.SQLException;

public interface GestoreOrdiniDAO {
	public Boolean doSaveGestore(GestoreOrdini gestoreOrdini) throws SQLException;

	public Boolean doUpdateGestore(GestoreOrdini gestoreOrdini) throws SQLException;

	public Boolean doDeleteGestore(String email) throws SQLException;
	
	public GestoreOrdini doRetrieveByAuthentication(String email, String password) throws SQLException;
}
