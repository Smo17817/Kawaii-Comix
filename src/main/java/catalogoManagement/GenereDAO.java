package catalogoManagement;

import java.sql.SQLException;
import java.util.Collection;

public interface GenereDAO {
	public Collection<String> doRetrieveAll() throws SQLException;
	// Controlla che il nome del genere inserito sia valido 
	public Boolean checkGenereName(String genereName) throws SQLException;
}
