package catalogoManagement;

import java.sql.SQLException;
import java.util.Collection;

public interface CategoriaDAO {
	public Collection<String> doRetrieveAll() throws SQLException;
	// Controlla che il nome della categoria inserita sia valido 
	public Boolean checkCategoriaName(String categoriaName) throws SQLException;
}
