package acquistoManagement;

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
import java.util.Collection;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.opentest4j.ArgumentsAreDifferent;

import utenteManagement.PasswordUtils;
import utenteManagement.User;
import utenteManagement.UserIDS;

public class GestoreOrdiniIDSTest {
	
	private DataSource ds;
	private Connection connection;
	private GestoreOrdiniIDS gestoreOrdiniIDS;
	
	@BeforeEach
    public void setUp() throws Exception {
  
        ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection())
                .thenReturn(connection = mock(Connection.class));
        gestoreOrdiniIDS = new GestoreOrdiniIDS(ds);
    }
	
	@Test
    @DisplayName("TCU doSaveGestoreTestSalva")
    public void doSaveGestoreTestSalva() throws Exception {
        // Mock del preparedStatement
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);

     // Configura il mock per ritornare il preparedStatement quando il metodo prepareStatement viene chiamato sulla connessione
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        GestoreOrdini gestoreOrdini = new GestoreOrdini("email","nome", "cognome", "Password");
        assertTrue(gestoreOrdiniIDS.doSaveGestore(gestoreOrdini));
        // Verifica che il metodo setString sia stato chiamato con i valori corretti
        Mockito.verify(preparedStatement, times(1)).setString(1, gestoreOrdini.getEmail());
        Mockito.verify(preparedStatement, times(1)).setString(2, gestoreOrdini.getNome());
        Mockito.verify(preparedStatement, times(1)).setString(3, gestoreOrdini.getCognome());
        Mockito.verify(preparedStatement, times(1)).setString(4, PasswordUtils.hashPassword(gestoreOrdini.getPassword()));

        // Verifica che il metodo executeUpdate sia stato chiamato
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
        
    }
	
	
	@Test
    @DisplayName("TCU doUpdateGestoreTest-Gestore Ordini Aggiornato")
    public void doUpdateGestoreTestAggiorna() throws Exception {
        // Mock del preparedStatement
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);

     // Configura il mock per ritornare il preparedStatement quando il metodo prepareStatement viene chiamato sulla connessione
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        GestoreOrdini gestoreOrdini = new GestoreOrdini("gestoreOrdini@gmail.com","gestore", "ordini", "PasswordGestoreOrdini");
        assertTrue(gestoreOrdiniIDS.doUpdateGestore(gestoreOrdini));
        // Verifica che il metodo setString sia stato chiamato con i valori corretti
        Mockito.verify(preparedStatement, times(1)).setString(1, gestoreOrdini.getNome());
        Mockito.verify(preparedStatement, times(1)).setString(2, gestoreOrdini.getCognome());
        Mockito.verify(preparedStatement, times(1)).setString(3, PasswordUtils.hashPassword(gestoreOrdini.getPassword()));
        Mockito.verify(preparedStatement, times(1)).setString(4,  gestoreOrdini.getEmail());


        // Verifica che il metodo executeUpdate sia stato chiamato
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
        
    }
	
	
	@Test
    @DisplayName("TCU DeleteGEstoreTest-Gestore Ordini Eliminato")
    public void doDeleteGestoreTestElimina() throws Exception {
	
        // Mock del preparedStatement
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);

     // Configura il mock per ritornare il preparedStatement quando il metodo prepareStatement viene chiamato sulla connessione
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        
        assertTrue(gestoreOrdiniIDS.doDeleteGestore("gestoreOrdini@gmail.com"));
        
        Mockito.verify(preparedStatement, times(1)).setString(1, "gestoreOrdini@gmail.com");
        Mockito.verify(preparedStatement, times(1)).executeUpdate();         
    }

    @Test
    @DisplayName("TCU doUpdateGestoreTest-Gestore Ordini Non Eliminato")
    public void doDeleteGestoreTestNonElimina() throws Exception {

        // Mock del preparedStatement
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(0);

        // Configura il mock per ritornare il preparedStatement quando il metodo prepareStatement viene chiamato sulla connessione
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        assertFalse(gestoreOrdiniIDS.doDeleteGestore("gestoreOrdini@gmail.com"));

        Mockito.verify(preparedStatement, times(1)).setString(1, "gestoreOrdini@gmail.com");
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
    }
	
	@Test
    @DisplayName("doRetrieveGestoreOrdiniTest-Gestore Trovato")
    public void doRetrieveGestoreOrdiniTest() throws SQLException {
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
        GestoreOrdini result = gestoreOrdiniIDS.doRetrieveByAuthentication("gestoreOrdini@gmail.com", "hashedPassword");

    
        boolean verifiedPassword;

        verifiedPassword = passwordUtils.verifyPassword("hashedPassword", result.getPassword());

        assertEquals(true, verifiedPassword);

        // Verifiche
        assertEquals("gestoreOrdini@gmail.com", result.getEmail());
        assertEquals("Mario", result.getNome());
        assertEquals("Rossi", result.getCognome());

        // Verifiche delle chiamate ai metodi
        Mockito.verify(preparedStatement, times(1)).setString(1, "gestoreOrdini@gmail.com");
        Mockito.verify(preparedStatement, times(1)).setString(2, passwordUtils.hashPassword("hashedPassword"));
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(1)).next();
        Mockito.verify(resultSet, times(1)).getString("nome");
        Mockito.verify(resultSet, times(1)).getString("cognome");
    }
	
	@Test
    @DisplayName("doRetrieveGestoreOrdiniTest-Email o password errate")
    public void doRetrieveGestoreOrdiniDatiErrati() throws SQLException {
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        PasswordUtils passwordUtils = Mockito.mock(PasswordUtils.class);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Mockito.when(resultSet.next()).thenReturn(false); // non ci sono risultati

        // Chiamo il metodo da testare
        GestoreOrdini result = gestoreOrdiniIDS.doRetrieveByAuthentication("gestoreOrdini@gmail.com", "hashedPassword");

        assertNull(result);

        // Verifiche delle chiamate ai metodi
        Mockito.verify(preparedStatement, times(1)).setString(1, "gestoreOrdini@gmail.com");
        Mockito.verify(preparedStatement, times(1)).setString(2, passwordUtils.hashPassword("hashedPassword"));
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(1)).next();
   
    }
	
	
	
}
