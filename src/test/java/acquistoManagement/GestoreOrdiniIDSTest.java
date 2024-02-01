package acquistoManagement;

import static org.junit.Assert.assertNull;
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
        
     // Configura il mock per ritornare il preparedStatement quando il metodo prepareStatement viene chiamato sulla connessione
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        GestoreOrdini gestoreOrdini = new GestoreOrdini("email","nome", "cognome", "Password");
        gestoreOrdiniIDS.doSaveGestore(gestoreOrdini);
        // Verifica che il metodo setString sia stato chiamato con i valori corretti
        Mockito.verify(preparedStatement, times(1)).setString(1, gestoreOrdini.getEmail());
        Mockito.verify(preparedStatement, times(1)).setString(2, gestoreOrdini.getNome());
        Mockito.verify(preparedStatement, times(1)).setString(3, gestoreOrdini.getCognome());
        Mockito.verify(preparedStatement, times(1)).setString(4, PasswordUtils.hashPassword(gestoreOrdini.getPassword()));

        // Verifica che il metodo executeUpdate sia stato chiamato
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
        
    }
	
	@Test
    @DisplayName("TCU doSaveGestoreTestNonSalva")
    public void doSaveGestoreTestNonSalva() throws Exception {
        // Mock del preparedStatement
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        boolean flag = false;
     // Configura il mock per ritornare il preparedStatement quando il metodo prepareStatement viene chiamato sulla connessione
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        GestoreOrdini gestoreOrdini = new GestoreOrdini("email","nome", "cognome", "Password");
        gestoreOrdiniIDS.doSaveGestore(gestoreOrdini);
        
        try {
        // Verifica che il metodo setString sia stato chiamato con i valori corretti
        	Mockito.verify(preparedStatement, times(1)).setString(1, gestoreOrdini.getEmail());
        	Mockito.verify(preparedStatement, times(1)).setString(3, gestoreOrdini.getNome());
        	Mockito.verify(preparedStatement, times(1)).setString(2, gestoreOrdini.getCognome());
        	Mockito.verify(preparedStatement, times(1)).setString(4, PasswordUtils.hashPassword(gestoreOrdini.getPassword()));
        }
        
        catch(ArgumentsAreDifferent e){	
        	flag = true;	
        }
        // Verifica che il metodo executeUpdate sia stato chiamato
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
        
        assertEquals(true, flag);
        
    }
	
	@Test
    @DisplayName("TCU doUpdateGestoreTest-Gestore Ordini Aggiornato")
    public void doUpdateGestoreTestAggiorna() throws Exception {
        // Mock del preparedStatement
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        
     // Configura il mock per ritornare il preparedStatement quando il metodo prepareStatement viene chiamato sulla connessione
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        GestoreOrdini gestoreOrdini = new GestoreOrdini("gestoreOrdini@gmail.com","gestore", "ordini", "PasswordGestoreOrdini");
        gestoreOrdiniIDS.doUpdateGestore(gestoreOrdini);
        // Verifica che il metodo setString sia stato chiamato con i valori corretti
        Mockito.verify(preparedStatement, times(1)).setString(1, gestoreOrdini.getNome());
        Mockito.verify(preparedStatement, times(1)).setString(2, gestoreOrdini.getCognome());
        Mockito.verify(preparedStatement, times(1)).setString(3, PasswordUtils.hashPassword(gestoreOrdini.getPassword()));
        Mockito.verify(preparedStatement, times(1)).setString(4,  gestoreOrdini.getEmail());

        // Verifica che il metodo executeUpdate sia stato chiamato
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
        
    }
	
	@Test
    @DisplayName("TCU doUpdateGestoreTest-Gestore Ordini Non Aggiornato")
    public void doUpdateGestoreTestNonAggiorna() throws Exception {
		
		boolean flag = true;
        // Mock del preparedStatement
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        
     // Configura il mock per ritornare il preparedStatement quando il metodo prepareStatement viene chiamato sulla connessione
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        GestoreOrdini gestoreOrdini = new GestoreOrdini("gestoreOrdini@gmail.com","gestore", "ordini", "PasswordGestoreOrdini");
        gestoreOrdiniIDS.doUpdateGestore(gestoreOrdini);
        
        try {
        // Verifica che il metodo setString sia stato chiamato con i valori corretti
        	Mockito.verify(preparedStatement, times(1)).setString(2, gestoreOrdini.getNome());
        	Mockito.verify(preparedStatement, times(1)).setString(1, gestoreOrdini.getCognome());
        	Mockito.verify(preparedStatement, times(1)).setString(3, PasswordUtils.hashPassword(gestoreOrdini.getPassword()));
        	Mockito.verify(preparedStatement, times(1)).setString(4,  gestoreOrdini.getEmail());
        }
        catch(ArgumentsAreDifferent e){
        	
        	flag = true;
        	
        }
        // Verifica che il metodo executeUpdate sia stato chiamato
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
        
        assertEquals(true, flag);
        
    }
	
}
