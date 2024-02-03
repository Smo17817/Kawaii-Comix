package acquistoManagement;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
import java.util.Date;
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

import catalogoManagement.Prodotto;
import catalogoManagement.ProdottoIDS;
import utenteManagement.PasswordUtils;
import utenteManagement.User;

public class OrdineSingoloIDSTest {

	
    private DataSource ds;
    private OrdineSingoloIDS ordineSingoloIDS;
    private Connection connection;
    private Prodotto prodotto;
    private PreparedStatement preparedStatement;
    
    @BeforeEach
    public void setUp() throws Exception {
  
        ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection())
                .thenReturn(connection = mock(Connection.class));
        ordineSingoloIDS = new OrdineSingoloIDS(ds);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        preparedStatement=mock(PreparedStatement.class);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        prodotto = mock(Prodotto.class);
        when(prodotto.getIsbn()).thenReturn("12345678901234567");


    }
    
    @Test
    @DisplayName("TCU doSaveOrdineSingoloTestSalva")
    public void doSaveOrdineSingoloTestSalva() throws Exception {
        // Mock del preparedStatement
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
     // Configura il mock per ritornare il preparedStatement quando il metodo prepareStatement viene chiamato sulla connessione
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        OrdineSingolo ordineSingolo = new OrdineSingolo(1, 10.0, 1, prodotto);

        assertTrue(ordineSingoloIDS.doSaveOrdineSingolo(ordineSingolo));
        // Verifica che il metodo setString sia stato chiamato con i valori corretti
        Mockito.verify(preparedStatement, times(1)).setInt(1, ordineSingolo.getQuantita());
        Mockito.verify(preparedStatement, times(1)).setDouble(2, ordineSingolo.getTotParziale());
        Mockito.verify(preparedStatement, times(1)).setInt(3, ordineSingolo.getOrdineId());
        Mockito.verify(preparedStatement, times(1)).setString(4, ordineSingolo.getProdotto().getIsbn() );
  
        // Verifica che il metodo executeUpdate sia stato chiamato
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
        
    }
    
    @Test
    @DisplayName("TCU doSaveOrdineSingoloTestNonSalva")
    public void doSaveOrdineSingoloTestNonSalva() throws Exception {
        // Mock del preparedStatement
        preparedStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(preparedStatement.executeUpdate()).thenReturn(0);
     // Configura il mock per ritornare il preparedStatement quando il metodo prepareStatement viene chiamato sulla connessione
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        OrdineSingolo ordineSingolo = new OrdineSingolo(1, 10.0, 1, prodotto);

        assertFalse(ordineSingoloIDS.doSaveOrdineSingolo(ordineSingolo));
        // Verifica che il metodo setString sia stato chiamato con i valori corretti
        Mockito.verify(preparedStatement, times(1)).setInt(1, ordineSingolo.getQuantita());
        Mockito.verify(preparedStatement, times(1)).setDouble(2, ordineSingolo.getTotParziale());
        Mockito.verify(preparedStatement, times(1)).setInt(3, ordineSingolo.getOrdineId());
        Mockito.verify(preparedStatement, times(1)).setString(4, ordineSingolo.getProdotto().getIsbn() );
  
        // Verifica che il metodo executeUpdate sia stato chiamato
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
        
    }
    
    @Test
    @DisplayName("TCU doDeleteOrdineSingolo- Ordine Singolo Cancellato")
    public void doDeleteOrdineSingoloTest() throws Exception{
        preparedStatement = Mockito.mock(PreparedStatement.class);
        
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);

        
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        OrdineSingolo ordineSingolo = new OrdineSingolo(1,1, 10.0, 1, prodotto);

        boolean check = ordineSingoloIDS.doDeleteOrdineSingolo(1);

        assertTrue(check);
        Mockito.verify(preparedStatement, times(1)).setInt(1, ordineSingolo.getId());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();

    }
    
    @Test
    @DisplayName("TCU doDeleteOrdineSingolo- Ordine Singolo Non Cancellato")
    public void doDeleteOrdineSingoloTestNonElimina() throws Exception{
        
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(0);
        OrdineSingolo ordineSingolo = new OrdineSingolo(1,1, 10.0, 1, prodotto);

        boolean check = ordineSingoloIDS.doDeleteOrdineSingolo(1);
                
        assertFalse(check);
        
        Mockito.verify(preparedStatement, times(1)).setInt(1, ordineSingolo.getId());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();

    }
    
    @Test
    @DisplayName("doUpdateStatoOrdine - Aggiorna")
    void  testDoUpdateAggiorna() throws SQLException{

    	// Creazione di un oggetto Ordine da testare
        // Verifica delle chiamate ai metodi di mock
    	when(preparedStatement.executeUpdate()).thenReturn(1);

        // Chiamo il metodo da testare
        boolean result = ordineSingoloIDS.doUpdateOrdineSingolo(1,1, 10.0, 1, prodotto);

        // Verifica delle chiamate ai metodi di mock
        verify(connection, times(1)).prepareStatement(anyString());
        verify(preparedStatement, times(1)).setInt(1, 1);
        verify(preparedStatement, times(1)).setDouble(2, 10.0);
        verify(preparedStatement, times(1)).setInt(3, 1);
        verify(preparedStatement, times(1)).setString(4, prodotto.getIsbn());
        verify(preparedStatement, times(1)).executeUpdate();

        // Verifica del risultato del metodo
        assertTrue(result);
    }
    
    @Test
    @DisplayName("doUpdateStatoOrdine - NonAggiorna")
    void  testDoUpdateNonAggiorna() throws SQLException{

    	// Creazione di un oggetto Ordine da testare
        // Verifica delle chiamate ai metodi di mock
    	when(preparedStatement.executeUpdate()).thenReturn(0);

        // Chiamo il metodo da testare
        boolean result = ordineSingoloIDS.doUpdateOrdineSingolo(1,1, 10.0, 1, prodotto);

        // Verifica delle chiamate ai metodi di mock
        verify(connection, times(1)).prepareStatement(anyString());
        verify(preparedStatement, times(1)).setInt(1, 1);
        verify(preparedStatement, times(1)).setDouble(2, 10.0);
        verify(preparedStatement, times(1)).setInt(3, 1);
        verify(preparedStatement, times(1)).setString(4, prodotto.getIsbn());
        verify(preparedStatement, times(1)).executeUpdate();

        // Verifica del risultato del metodo
        assertFalse(result);
    }
    
    @Test
    @DisplayName("TCU doRetrieveAllOrdiniSingoliTest")
    public void doRetrieveAllOrdiniSingoliTest() throws Exception {
        ProdottoIDS prodottoIDS =Mockito.mock(ProdottoIDS.class);
    	when(prodottoIDS.doRetrieveByIsbn(anyString())).thenReturn(prodotto);
    	ResultSet resultSet = Mockito.mock(ResultSet.class);
    	String isbnMock = prodottoIDS.doRetrieveByIsbn(prodotto.getIsbn());

        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Mockito.when(resultSet.next()).thenReturn(true, true,false); // Prima e seconda chiamata restituiscono true, la terza restituisce false
        Mockito.when(resultSet.getInt("id")).thenReturn(1, 2);
        Mockito.when(resultSet.getInt("quantità")).thenReturn(10,11);
        Mockito.when(resultSet.getDouble("totale_parziale")).thenReturn(10.5, 9.5);
        Mockito.when(resultSet.getInt("ordini_id")).thenReturn(100, 112);
        Mockito.when(resultSet.getString("prodotti_isbn")).thenReturn(, "76543210987654321");
       

        Collection<OrdineSingolo> result = ordineSingoloIDS.doRetrieveAllOrdineSingolo();

        assertEquals(2, result.size());

        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(3)).next();
        Mockito.verify(resultSet, times(2)).getInt("id");
        Mockito.verify(resultSet, times(2)).getString("quantità");
        Mockito.verify(resultSet, times(2)).getString("totale_parziale");
        Mockito.verify(resultSet, times(2)).getString("ordini_id");
        Mockito.verify(resultSet, times(2)).getString("prodotti_isbn");
       
        
    }
}
