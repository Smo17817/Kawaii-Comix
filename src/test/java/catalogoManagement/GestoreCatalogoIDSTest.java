package catalogoManagement;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import utenteManagement.PasswordUtils;

public class GestoreCatalogoIDSTest {
	
	private DataSource ds;
	private Connection connection;
	private GestoreCatalogoIDS gestoreCatalogoIDS;
	
	@BeforeEach
    public void setUp() throws Exception {
  
        ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection())
                .thenReturn(connection = mock(Connection.class));
        gestoreCatalogoIDS = new GestoreCatalogoIDS(ds);
    }
	
	@Test
    @DisplayName("TCU doSaveGestoreTestSalva")
    public void doSaveGestoreTestSalva() throws Exception {
        // Mock del preparedStatement
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);

     // Configura il mock per ritornare il preparedStatement quando il metodo prepareStatement viene chiamato sulla connessione
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        GestoreCatalogo gestoreCatalogo = new GestoreCatalogo("email","nome", "cognome", "Password");
        assertTrue(gestoreCatalogoIDS.doSaveGestore(gestoreCatalogo));
        // Verifica che il metodo setString sia stato chiamato con i valori corretti
        Mockito.verify(preparedStatement, times(1)).setString(1, gestoreCatalogo.getEmail());
        Mockito.verify(preparedStatement, times(1)).setString(2, gestoreCatalogo.getNome());
        Mockito.verify(preparedStatement, times(1)).setString(3, gestoreCatalogo.getCognome());
        Mockito.verify(preparedStatement, times(1)).setString(4, PasswordUtils.hashPassword(gestoreCatalogo.getPassword()));

        // Verifica che il metodo executeUpdate sia stato chiamato
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
        
    }
	
	
	@Test
    @DisplayName("TCU doUpdateGestoreTest-Gestore Catalogo Aggiornato")
    public void doUpdateGestoreTestAggiorna() throws Exception {
        // Mock del preparedStatement
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);

     // Configura il mock per ritornare il preparedStatement quando il metodo prepareStatement viene chiamato sulla connessione
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        GestoreCatalogo gestoreCatalogo = new GestoreCatalogo("gestoreCatalogo@gmail.com","gestore", "catalogo", "PasswordGestoreCatalogo");
        assertTrue(gestoreCatalogoIDS.doUpdateGestore(gestoreCatalogo));
        // Verifica che il metodo setString sia stato chiamato con i valori corretti
        Mockito.verify(preparedStatement, times(1)).setString(1, gestoreCatalogo.getNome());
        Mockito.verify(preparedStatement, times(1)).setString(2, gestoreCatalogo.getCognome());
        Mockito.verify(preparedStatement, times(1)).setString(3, PasswordUtils.hashPassword(gestoreCatalogo.getPassword()));
        Mockito.verify(preparedStatement, times(1)).setString(4,  gestoreCatalogo.getEmail());


        // Verifica che il metodo executeUpdate sia stato chiamato
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
        
    }
	
	
	@Test
    @DisplayName("TCU doUpdateGestoreTest-Gestore Catalogo Eliminato")
    public void doDeleteGestoreTestElimina() throws Exception {
	
        // Mock del preparedStatement
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);

     // Configura il mock per ritornare il preparedStatement quando il metodo prepareStatement viene chiamato sulla connessione
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        
        assertTrue(gestoreCatalogoIDS.doDeleteGestore("gestoreCatalogo@gmail.com"));
        
        Mockito.verify(preparedStatement, times(1)).setString(1, "gestoreCatalogo@gmail.com");
        Mockito.verify(preparedStatement, times(1)).executeUpdate();         
    }
	
	@Test
    @DisplayName("doRetrieveGestoreCatalogoTest-Gestore Trovato")
    public void doRetrieveGestoreCatalogoTest() throws SQLException {
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        PasswordUtils passwordUtils = Mockito.mock(PasswordUtils.class);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        /* Utente da trovare */
        Mockito.when(resultSet.next()).thenReturn(true); // Ci sono risultati
        Mockito.when(resultSet.getString("nome")).thenReturn("Mario");
        Mockito.when(resultSet.getString("cognome")).thenReturn("Rossi");



        // Chiamo il metodo da testare
        GestoreCatalogo result = gestoreCatalogoIDS.doRetrieveByAuthentication("gestoreCatalogo@gmail.com", "hashedPassword");

    
        boolean verifiedPassword;

        verifiedPassword = passwordUtils.verifyPassword("hashedPassword", result.getPassword());

        assertEquals(true, verifiedPassword);

        // Verifiche
        assertEquals("gestoreCatalogo@gmail.com", result.getEmail());
        assertEquals("Mario", result.getNome());
        assertEquals("Rossi", result.getCognome());

        // Verifiche delle chiamate ai metodi
        Mockito.verify(preparedStatement, times(1)).setString(1, "gestoreCatalogo@gmail.com");
        Mockito.verify(preparedStatement, times(1)).setString(2, passwordUtils.hashPassword("hashedPassword"));
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(1)).next();
        Mockito.verify(resultSet, times(1)).getString("nome");
        Mockito.verify(resultSet, times(1)).getString("cognome");
    }
	@Test
    @DisplayName("doRetrieveGestoreCatalogoTest-Email o password errate")
    public void doRetrieveGestoreCatalogoDatiErrati() throws SQLException {
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        PasswordUtils passwordUtils = Mockito.mock(PasswordUtils.class);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Mockito.when(resultSet.next()).thenReturn(false); // non ci sono risultati

        // Chiamo il metodo da testare
        GestoreCatalogo result = gestoreCatalogoIDS.doRetrieveByAuthentication("gestoreCatalogo@gmail.com", "hashedPassword");

        assertNull(result);

        // Verifiche delle chiamate ai metodi
        Mockito.verify(preparedStatement, times(1)).setString(1, "gestoreCatalogo@gmail.com");
        Mockito.verify(preparedStatement, times(1)).setString(2, passwordUtils.hashPassword("hashedPassword"));
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(1)).next();
   
    }
	
}
