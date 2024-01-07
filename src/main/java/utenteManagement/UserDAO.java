package utenteManagement;

import java.sql.SQLException;
import java.util.Collection;

public interface UserDAO {
	public void doSaveUser(User user) throws SQLException;

	public Boolean doDeleteUser(String id) throws SQLException;

	public Boolean doUpdateUser(User user) throws SQLException;

	public Collection<User> doRetrieveAllUsers() throws SQLException;

	public User doRetrieveById(Integer id) throws SQLException;
	
	public User doRetrieveUser(String email, String password) throws SQLException;
}
