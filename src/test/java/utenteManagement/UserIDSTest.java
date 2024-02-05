package utenteManagement;

import static org.junit.Assert.*;
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

public class UserIDSTest {

	
    private DataSource ds;
    private UserIDS userIDS;
    private Connection connection;
    
    
    
    @BeforeEach
    public void setUp() throws Exception {
  
        ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection())
                .thenReturn(connection = mock(Connection.class));
        userIDS = new UserIDS(ds);
    }

    
    @Test
    @DisplayName("TCU1_1_1 doSaveUserTestSalva")
    public void doSaveUserTestSalva() throws Exception {
        // Mock del preparedStatement
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        PasswordUtils passwordUtils = Mockito.mock(PasswordUtils.class);

        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
     // Configura il mock per ritornare il preparedStatement quando il metodo prepareStatement viene chiamato sulla connessione
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        User user = new User("mariorossi@gmail.com", "Prova123", "Mario", "Rossi", "Via Roma 1", "Salerno", "84100", "SA", "Italia");

        assertTrue(userIDS.doSaveUser(user));
        // Verifica che il metodo setString sia stato chiamato con i valori corretti
        Mockito.verify(preparedStatement, times(1)).setString(1, user.getEmail());
        Mockito.verify(preparedStatement, times(1)).setString(2, passwordUtils.hashPassword(user.getPassword()));
        Mockito.verify(preparedStatement, times(1)).setString(3, user.getNome());
        Mockito.verify(preparedStatement, times(1)).setString(4, user.getCognome());
        Mockito.verify(preparedStatement, times(1)).setString(5, user.getIndirizzo());
        Mockito.verify(preparedStatement, times(1)).setString(6, user.getCitta());
        Mockito.verify(preparedStatement, times(1)).setString(7, user.getCap());
        Mockito.verify(preparedStatement, times(1)).setString(8, user.getProvincia());
        Mockito.verify(preparedStatement, times(1)).setString(9, user.getNazione());

        // Verifica che il metodo executeUpdate sia stato chiamato
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
        
    }

    @Test
    @DisplayName("TCU1_1_2 doNotSaveUserTestSalva-Utente Non Salvato")
    public void doNotSaveUserTestSalva() throws Exception {
        // Mock del preparedStatement
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        PasswordUtils passwordUtils = Mockito.mock(PasswordUtils.class);

        Mockito.when(preparedStatement.executeUpdate()).thenReturn(0);
        // Configura il mock per ritornare il preparedStatement quando il metodo prepareStatement viene chiamato sulla connessione
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        User user = new User("mariorossi@gmail.com", "Prova123", "Mario", "Rossi", "Via Roma 1", "Salerno", "84100", "SA", "Italia");
        assertFalse(userIDS.doSaveUser(user));

    // Verifica che il metodo setString sia stato chiamato con i valori corretti
        Mockito.verify(preparedStatement, times(1)).setString(1, user.getEmail());
        Mockito.verify(preparedStatement, times(1)).setString(2, passwordUtils.hashPassword(user.getPassword()));
        Mockito.verify(preparedStatement, times(1)).setString(3, user.getNome());
        Mockito.verify(preparedStatement, times(1)).setString(4, user.getCognome());
        Mockito.verify(preparedStatement, times(1)).setString(5, user.getIndirizzo());
        Mockito.verify(preparedStatement, times(1)).setString(6, user.getCitta());
        Mockito.verify(preparedStatement, times(1)).setString(7, user.getCap());
        Mockito.verify(preparedStatement, times(1)).setString(8, user.getProvincia());
        Mockito.verify(preparedStatement, times(1)).setString(9, user.getNazione());



        // Verifica che il metodo executeUpdate sia stato chiamato
        Mockito.verify(preparedStatement, times(1)).executeUpdate();

    }
    
    @Test
    @DisplayName("TCU1_2_1 doDeleteUserTest- Utente Cancellato")
    public void doDeleteUserTest() throws Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        User user = new User(1, "mariorossi@gmail.com", "Prova123", "Mario", "Rossi", "Via Roma 1", "Salerno", "84100", "SA", "Italia");

        boolean check = userIDS.doDeleteUser(user.getId());

        assertEquals(true, check);
        Mockito.verify(preparedStatement, times(1)).setInt(1, user.getId());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();

    }

    @Test
    @DisplayName("TCU1_2_2 doNotDeleteUserTest- Utente Non Cancellato")
    public void doNotDeleteUserTest() throws Exception {
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);

        // Configura il mock per restituire 0 quando executeUpdate() viene chiamato
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(0);

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        User user = new User(1, "mariorossi@gmail.com", "Prova123", "Mario", "Rossi", "Via Roma 1", "Salerno", "84100", "SA", "Italia");

        boolean check = userIDS.doDeleteUser(user.getId());

        assertFalse(check); // Verifica che il metodo restituisca false
        Mockito.verify(preparedStatement, times(1)).setInt(1, user.getId());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
    }
    
    @Test
    @DisplayName("TCU1_3_1 doUpdateUserTest- Utente Aggiornato")
    public void doUpdateUserTest() throws Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        User user = new User(1, "mariorossi@gmail.com", "Prova123", "Mario", "Rossi", "Via Roma 1", "Salerno", "84100", "SA", "Italia");

         assertTrue(userIDS.doUpdateUser(user));

        Mockito.verify(preparedStatement, times(1)).setString(1, user.getEmail());
        Mockito.verify(preparedStatement, times(1)).setString(2, PasswordUtils.hashPassword(user.getPassword()));
        Mockito.verify(preparedStatement, times(1)).setString(3, user.getNome());
        Mockito.verify(preparedStatement, times(1)).setString(4, user.getCognome());
        Mockito.verify(preparedStatement, times(1)).setString(5, user.getIndirizzo());
        Mockito.verify(preparedStatement, times(1)).setString(6, user.getCitta());
        Mockito.verify(preparedStatement, times(1)).setString(7, user.getCap());
        Mockito.verify(preparedStatement, times(1)).setString(8, user.getProvincia());
        Mockito.verify(preparedStatement, times(1)).setString(9, user.getNazione());
        Mockito.verify(preparedStatement, times(1)).setInt(10, user.getId());

        Mockito.verify(preparedStatement, times(1)).executeUpdate();
    }


    @Test
    @DisplayName("TCU1_3_2 doNotUpdateUserTest- Utente Non Aggiornato")
    public void doNotUpdateUserTest() throws Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(0);

        User user = new User(1, "mariorossi@gmail.com", "Prova123", "Mario", "Rossi", "Via Roma 1", "Salerno", "84100", "SA", "Italia");

       boolean check = userIDS.doUpdateUser(user);
       assertFalse(check);

        Mockito.verify(preparedStatement, times(1)).setString(1, user.getEmail());
        Mockito.verify(preparedStatement, times(1)).setString(2, PasswordUtils.hashPassword(user.getPassword()));
        Mockito.verify(preparedStatement, times(1)).setString(3, user.getNome());
        Mockito.verify(preparedStatement, times(1)).setString(4, user.getCognome());
        Mockito.verify(preparedStatement, times(1)).setString(5, user.getIndirizzo());
        Mockito.verify(preparedStatement, times(1)).setString(6, user.getCitta());
        Mockito.verify(preparedStatement, times(1)).setString(7, user.getCap());
        Mockito.verify(preparedStatement, times(1)).setString(8, user.getProvincia());
        Mockito.verify(preparedStatement, times(1)).setString(9, user.getNazione());
        Mockito.verify(preparedStatement, times(1)).setInt(10, user.getId());


        Mockito.verify(preparedStatement, times(1)).executeUpdate();
    }
    
    
    @Test
    @DisplayName("TCU1_4_1 doUpdateUserPasswordTest- Utente presente")
    public void doUpdateUserPasswordTest() throws Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        PasswordUtils passwordUtils = Mockito.mock(PasswordUtils.class);

        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        assertTrue( userIDS.doUpdateUserPassword("mariorossi@gmail.com", "hashedPassword"));

        Mockito.verify(preparedStatement, times(1)).executeUpdate();
        Mockito.verify(preparedStatement, times(1)).setString(1, passwordUtils.hashPassword("hashedPassword"));
        Mockito.verify(preparedStatement, times(1)).setString(2, "mariorossi@gmail.com");
    }

    @Test
    @DisplayName("TCU1_4_2 doNotUpdateUserPasswordTest- Utente Non presente")
    public void doNotUpdateUserPasswordTest() throws Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        PasswordUtils passwordUtils = Mockito.mock(PasswordUtils.class);

        Mockito.when(preparedStatement.executeUpdate()).thenReturn(0);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

       assertFalse(userIDS.doUpdateUserPassword("mariorossi@gmail.com", "hashedPassword"));

        Mockito.verify(preparedStatement, times(1)).executeUpdate();
        Mockito.verify(preparedStatement, times(1)).setString(1, passwordUtils.hashPassword("hashedPassword"));
        Mockito.verify(preparedStatement, times(1)).setString(2, "mariorossi@gmail.com");

    }
    
    @Test
    @DisplayName("TCU1_5_1 doRetrieveAllUsersTest")
    public void doRetrieveAllUsersTest() throws Exception {
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Mockito.when(resultSet.next()).thenReturn(true, true,false); // Prima e seconda chiamata restituiscono true, la terza restituisce false
        Mockito.when(resultSet.getInt("id")).thenReturn(1, 2);
        Mockito.when(resultSet.getString("email_address")).thenReturn("mariorossi@gmail.com", "rossimario@gmail.com");
        Mockito.when(resultSet.getString("password")).thenReturn("hashedPassword1", "hashedPassword2");
        Mockito.when(resultSet.getString("nome")).thenReturn("Mario", "Rossi");
        Mockito.when(resultSet.getString("cognome")).thenReturn("Rossi", "Mario");
        Mockito.when(resultSet.getString("indirizzo")).thenReturn("Via Roma 1", "Via Roma 2");
        Mockito.when(resultSet.getString("citta")).thenReturn("Salerno", "Salerno");
        Mockito.when(resultSet.getString("codice_postale")).thenReturn("84100", "84100");
        Mockito.when(resultSet.getString("provincia")).thenReturn("SA", "SA");
        Mockito.when(resultSet.getString("nazione")).thenReturn("Italia", "Italia");

        Collection<User> result = userIDS.doRetrieveAllUsers();

        assertEquals(2, result.size());

        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(3)).next();
        Mockito.verify(resultSet, times(2)).getInt("id");
        Mockito.verify(resultSet, times(2)).getString("email_address");
        Mockito.verify(resultSet, times(2)).getString("password");
        Mockito.verify(resultSet, times(2)).getString("nome");
        Mockito.verify(resultSet, times(2)).getString("cognome");
        Mockito.verify(resultSet, times(2)).getString("indirizzo");
        Mockito.verify(resultSet, times(2)).getString("citta");
        Mockito.verify(resultSet, times(2)).getString("codice_postale");
        Mockito.verify(resultSet, times(2)).getString("provincia");
        Mockito.verify(resultSet, times(2)).getString("nazione");
        
    }
    
    @Test
    @DisplayName("TCU1_6_1 doRetrieveByIdTest")
    public void doRetrieveByIdTest() throws Exception {
        // Mock del preparedStatement
            	
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        // Configura il mock per ritornare il preparedStatement quando il metodo prepareStatement viene chiamato sulla connessione
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        // Configura il mock per ritornare un risultato simulato quando executeQuery() è chiamato
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Configura il mock per ritornare valori simulati quando chiamati i metodi del ResultSet
        Mockito.when(resultSet.next()).thenReturn(true); // Ci sono risultati
        Mockito.when(resultSet.getString("email_address")).thenReturn("mariorossi@gmail.com");
        Mockito.when(resultSet.getString("password")).thenReturn("hashedPassword");
        Mockito.when(resultSet.getString("nome")).thenReturn("Mario");
        Mockito.when(resultSet.getString("cognome")).thenReturn("Rossi");
        Mockito.when(resultSet.getString("indirizzo")).thenReturn("Via Roma 1");
        Mockito.when(resultSet.getString("citta")).thenReturn("Salerno");
        Mockito.when(resultSet.getString("codice_postale")).thenReturn("84100");
        Mockito.when(resultSet.getString("provincia")).thenReturn("SA");
        Mockito.when(resultSet.getString("nazione")).thenReturn("Italia");

        User result = userIDS.doRetrieveById(1);

        assertNotNull(result);
        // Verifica che il risultato sia quello atteso
        assertEquals("mariorossi@gmail.com", result.getEmail());
        assertEquals("hashedPassword", result.getPassword());
        assertEquals("Mario", result.getNome());
        assertEquals("Rossi", result.getCognome());
        assertEquals("Via Roma 1", result.getIndirizzo());
        assertEquals("Salerno", result.getCitta());
        assertEquals("84100", result.getCap());
        assertEquals("SA", result.getProvincia());
        assertEquals("Italia", result.getNazione());

        // Verifica che i metodi del preparedStatement siano stati chiamati correttamente
        Mockito.verify(preparedStatement, times(1)).setInt(1, 1);
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(1)).next();
        Mockito.verify(resultSet, times(1)).getString("email_address");
        Mockito.verify(resultSet, times(1)).getString("password");
        Mockito.verify(resultSet, times(1)).getString("nome");
        Mockito.verify(resultSet, times(1)).getString("cognome");
        Mockito.verify(resultSet, times(1)).getString("indirizzo");
        Mockito.verify(resultSet, times(1)).getString("citta");
        Mockito.verify(resultSet, times(1)).getString("codice_postale");
        Mockito.verify(resultSet, times(1)).getString("provincia");
        Mockito.verify(resultSet, times(1)).getString("nazione");
        
        resultSet.close();
    }
    
    @Test
    @DisplayName("TCU1_6_2 doRetrieveByIdTest - Utente non trovato")
    public void doRetrieveByIdTest_UserNotFound() throws Exception {
        // Mock del preparedStatement
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Configura il mock per ritornare false quando next() è chiamato, simulando l'assenza di risultati
        Mockito.when(resultSet.next()).thenReturn(false);

        User result = userIDS.doRetrieveById(1);

        // Verifica che il risultato sia null, poiché l'utente non è stato trovato
        assertNull(result);

        Mockito.verify(preparedStatement, times(1)).setInt(1, 1);
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(1)).next();  // L'utente non è stato trovato, quindi next() dovrebbe essere chiamato solo una volta
        
        resultSet.close();
    }

    @Test
    @DisplayName("TCU1_7_1 doRetrieveUserTest-Utente Trovato")
    public void doRetrieveUserTest() throws SQLException {
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        PasswordUtils passwordUtils = Mockito.mock(PasswordUtils.class);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        /* Utente da trovare */
        Mockito.when(resultSet.next()).thenReturn(true); // Ci sono risultati
        Mockito.when(resultSet.getString("password")).thenReturn(passwordUtils.hashPassword("hashedPassword"));
        Mockito.when(resultSet.getString("nome")).thenReturn("Mario");
        Mockito.when(resultSet.getString("cognome")).thenReturn("Rossi");
        Mockito.when(resultSet.getString("indirizzo")).thenReturn("Via Roma 1");
        Mockito.when(resultSet.getString("citta")).thenReturn("Salerno");
        Mockito.when(resultSet.getString("codice_postale")).thenReturn("84100");
        Mockito.when(resultSet.getString("provincia")).thenReturn("SA");
        Mockito.when(resultSet.getString("nazione")).thenReturn("Italia");



        // Chiamo il metodo da testare
        User result = userIDS.doRetrieveUser("mariorossi@gmail.com", "hashedPassword");

        boolean verifiedPassword;

        verifiedPassword = passwordUtils.verifyPassword("hashedPassword", result.getPassword());

        assertEquals(true, verifiedPassword);

        // Verifiche
        assertEquals("mariorossi@gmail.com", result.getEmail());
        assertEquals("Mario", result.getNome());
        assertEquals("Rossi", result.getCognome());
        assertEquals("Via Roma 1", result.getIndirizzo());
        assertEquals("Salerno", result.getCitta());
        assertEquals("84100", result.getCap());
        assertEquals("SA", result.getProvincia());
        assertEquals("Italia", result.getNazione());

        // Verifiche delle chiamate ai metodi
        Mockito.verify(preparedStatement, times(1)).setString(1, "mariorossi@gmail.com");
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(1)).next();
        Mockito.verify(resultSet, times(2)).getString("password");
        Mockito.verify(resultSet, times(1)).getString("nome");
        Mockito.verify(resultSet, times(1)).getString("cognome");
        Mockito.verify(resultSet, times(1)).getString("indirizzo");
        Mockito.verify(resultSet, times(1)).getString("citta");
        Mockito.verify(resultSet, times(1)).getString("codice_postale");
        Mockito.verify(resultSet, times(1)).getString("provincia");
        Mockito.verify(resultSet, times(1)).getString("nazione");

    }

    @Test
    @DisplayName("TCU1_7_2 doRetrieveUserTest-Utente Non Trovato")
    public void doRetrieveUserNonTrovatoTest() throws SQLException {
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        /* Utente da trovare */
        Mockito.when(resultSet.next()).thenReturn(false); // Non ci sono risultati

        // Chiamo il metodo da testare
        User result = userIDS.doRetrieveUser("mariorossi@gmail.com", "hashedPassword");

        assertNull(result);


        Mockito.verify(preparedStatement, times(1)).setString(1, "mariorossi@gmail.com");
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(1)).next();

        resultSet.close();
    }

    @Test
    @DisplayName("TCU1_7_3 doRetrieveUserTest-Utente Non Trovato Password Errata")
    public void doRetrieveUserPasswordErrataTest() throws SQLException {
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        PasswordUtils passwordUtils = Mockito.mock(PasswordUtils.class);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        /* Utente da trovare */
        Mockito.when(resultSet.next()).thenReturn(true); // Ci sono risultati
        Mockito.when(resultSet.getString("password")).thenReturn(passwordUtils.hashPassword("giovannisicilia"));
        Mockito.when(resultSet.getString("nome")).thenReturn("Mario");
        Mockito.when(resultSet.getString("cognome")).thenReturn("Rossi");
        Mockito.when(resultSet.getString("indirizzo")).thenReturn("Via Roma 1");
        Mockito.when(resultSet.getString("citta")).thenReturn("Salerno");
        Mockito.when(resultSet.getString("codice_postale")).thenReturn("84100");
        Mockito.when(resultSet.getString("provincia")).thenReturn("SA");
        Mockito.when(resultSet.getString("nazione")).thenReturn("Italia");


        User result = userIDS.doRetrieveUser("mariorossi@gmail.com", "hashedPassword");

        assertNull(result);


        // Verifiche delle chiamate ai metodi
        Mockito.verify(preparedStatement, times(1)).setString(1, "mariorossi@gmail.com");
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(1)).next();
        Mockito.verify(resultSet, times(1)).getString("password");

        resultSet.close();


    }




    @Test
    @DisplayName("TCU1_8_1 emailExists- Email Presente")
    public void emailExists() throws Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Mockito.when(resultSet.next()).thenReturn(true);
        //ci sono risultati
        assertTrue(userIDS.emailExists("mariorossi@gmail.com"));

        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(preparedStatement, times(1)).setString(1, "mariorossi@gmail.com");
        Mockito.verify(resultSet, times(1)).next();

    }

    @Test
    @DisplayName("TCU1_8_2 emailExists- Email Non Presente")
    public void emailNotExists() throws Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Mockito.when(resultSet.next()).thenReturn(false);
        //non ci sono risultati
        assertFalse(userIDS.emailExists("mariorossi@gmail.com"));

        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(preparedStatement, times(1)).setString(1, "mariorossi@gmail.com");
        Mockito.verify(resultSet, times(1)).next();

    }



} 
