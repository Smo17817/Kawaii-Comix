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
import org.mockito.Mock;
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
    @DisplayName("TCU doRetrieveAllOrdiniSingoliTest")//TODO Da rivedere
    public void doRetrieveAllOrdiniSingoliTest() throws Exception {
        ProdottoIDS prodottoIDS = Mockito.mock(ProdottoIDS.class);
        
        Prodotto prodotto1 = Mockito.mock(Prodotto.class);
        Prodotto prodotto2 = Mockito.mock(Prodotto.class);
        
        when(prodotto1.getIsbn()).thenReturn("12345678901234567");
        when(prodotto2.getIsbn()).thenReturn("76543210987654321");
        
        String isbnMock1=prodotto1.getIsbn();
        String isbnMock2=prodotto2.getIsbn();
        
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        
        when(prodottoIDS.doRetrieveByIsbn(prodotto1.getIsbn())).thenReturn(prodotto1);
        when(prodottoIDS.doRetrieveByIsbn(prodotto2.getIsbn())).thenReturn(prodotto2);
        
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getInt("id")).thenReturn(1, 2);
        when(resultSet.getInt("quantità")).thenReturn(10, 11);
        when(resultSet.getDouble("totale_parziale")).thenReturn(10.5, 9.5);
        when(resultSet.getInt("ordini_id")).thenReturn(100, 112);
        when(resultSet.getString("prodotti_isbn")).thenReturn(isbnMock1, isbnMock2);

        Collection<OrdineSingolo> result = ordineSingoloIDS.doRetrieveAllOrdineSingolo();

        assertEquals(2, result.size());

        Mockito.verify(preparedStatement, times(3)).executeQuery();
        Mockito.verify(resultSet, times(5)).next();
        Mockito.verify(resultSet, times(2)).getInt("id");
        Mockito.verify(resultSet, times(2)).getInt("quantità");
        Mockito.verify(resultSet, times(2)).getDouble("totale_parziale");
        Mockito.verify(resultSet, times(2)).getInt("ordini_id");
        Mockito.verify(resultSet, times(2)).getString("prodotti_isbn");
    }
    
    @Test
    @DisplayName("TCU doRetrieveAllOrdiniSingoliByOrdineId")
    void testDoRetrieveAllByOrdineId() throws SQLException {
    	ProdottoIDS prodottoIDS = Mockito.mock(ProdottoIDS.class);
    	
    	prodotto = mock(Prodotto.class);
    	
        when(prodotto.getIsbn()).thenReturn("12345678901234567");

        ResultSet resultSet = Mockito.mock(ResultSet.class);
        String isbnMock = prodotto.getIsbn();
        when(prodottoIDS.doRetrieveByIsbn(prodotto.getIsbn())).thenReturn(prodotto);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("quantità")).thenReturn(10);
        when(resultSet.getDouble("totale_parziale")).thenReturn(100.0);
        when(resultSet.getInt("ordini_id")).thenReturn(1);
        when(resultSet.getString("prodotti_isbn")).thenReturn(isbnMock);

        Collection<OrdineSingolo> result = ordineSingoloIDS.doRetrieveAllByOrdineId(1);

        assertEquals(1, result.size()); // Verifica che ci sia un solo OrdineSingolo nel risultato
        OrdineSingolo ordineSingolo = result.iterator().next();
        assertEquals(10, ordineSingolo.getQuantita());
        assertEquals(100.0, ordineSingolo.getTotParziale());
        assertEquals(1, ordineSingolo.getOrdineId());
        assertNotNull(ordineSingolo.getProdotto()); 
        
        Mockito.verify(preparedStatement, times(1)).setInt(1, ordineSingolo.getOrdineId());
        Mockito.verify(preparedStatement, times(2)).executeQuery();
        Mockito.verify(resultSet, times(3)).next();
        Mockito.verify(resultSet, times(1)).getInt("id");
        Mockito.verify(resultSet, times(1)).getInt("quantità");
        Mockito.verify(resultSet, times(1)).getDouble("totale_parziale");
        Mockito.verify(resultSet, times(1)).getString("prodotti_isbn");
    }
    
    @Test
    @DisplayName("TCU doRetrieveAllOrdiniSingoliByOrdineId - ordineId Non Trovato")
    void testDoRetrieveAllByOrdineIdNonTrovato() throws SQLException {
    	ProdottoIDS prodottoIDS = Mockito.mock(ProdottoIDS.class);
    	
    	prodotto = mock(Prodotto.class);
    	
        when(prodotto.getIsbn()).thenReturn("12345678901234567");
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(0);

        ResultSet resultSet = Mockito.mock(ResultSet.class);
        String isbnMock = prodotto.getIsbn();
        when(prodottoIDS.doRetrieveByIsbn(prodotto.getIsbn())).thenReturn(prodotto);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("quantità")).thenReturn(10);
        when(resultSet.getDouble("totale_parziale")).thenReturn(100.0);
        when(resultSet.getInt("ordini_id")).thenReturn(1);
        when(resultSet.getString("prodotti_isbn")).thenReturn(isbnMock);

        Collection<OrdineSingolo> result = ordineSingoloIDS.doRetrieveAllByOrdineId(1);

        assertEquals(0, result.size());
         
        Mockito.verify(preparedStatement, times(1)).setInt(1, 1);
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(1)).next();
    }
    
    @Test
    @DisplayName("TCU doRetrieveOrdiniSingoliById")
    void testDoRetrieveOrdineSingoloById() throws SQLException {
    	ProdottoIDS prodottoIDS = Mockito.mock(ProdottoIDS.class);
    	
        // Configurazione dello stubbing per il mock ProdottoIDS
    	prodotto = mock(Prodotto.class);
    	
        when(prodotto.getIsbn()).thenReturn("12345678901234567");

        ResultSet resultSet = Mockito.mock(ResultSet.class);
        String isbnMock = prodotto.getIsbn();
        when(prodottoIDS.doRetrieveByIsbn(prodotto.getIsbn())).thenReturn(prodotto);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("quantità")).thenReturn(10);
        when(resultSet.getDouble("totale_parziale")).thenReturn(100.0);
        when(resultSet.getInt("ordini_id")).thenReturn(1);
        when(resultSet.getString("prodotti_isbn")).thenReturn(isbnMock);

        // Chiamata al metodo da testare
        OrdineSingolo result = ordineSingoloIDS.doRetrieveById(1);

        // Verifica degli output attesi
        assertEquals(10, result.getQuantita());
        assertEquals(100.0, result.getTotParziale());
        assertEquals(1, result.getOrdineId());
        assertNotNull(result.getProdotto()); 
        Mockito.verify(preparedStatement, times(1)).setInt(1, result.getId());
        Mockito.verify(preparedStatement, times(2)).executeQuery();
        Mockito.verify(resultSet, times(2)).next();
        Mockito.verify(resultSet, times(1)).getInt("quantità");
        Mockito.verify(resultSet, times(1)).getDouble("totale_parziale");
        Mockito.verify(resultSet, times(1)).getInt("ordini_id");
        Mockito.verify(resultSet, times(1)).getString("prodotti_isbn");
    }
    
    @Test
    @DisplayName("TCU doRetrieveOrdineSingoloById - Id non Trovato")
    void testDoRetrieveByIdNonTrovato() throws SQLException {
    	ProdottoIDS prodottoIDS = Mockito.mock(ProdottoIDS.class);
    	
    	prodotto = mock(Prodotto.class);
    	
        when(prodotto.getIsbn()).thenReturn("12345678901234567");

        ResultSet resultSet = Mockito.mock(ResultSet.class);
        
        when(prodottoIDS.doRetrieveByIsbn(prodotto.getIsbn())).thenReturn(prodotto);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Configurazione dello stubbing per il mock ResultSet
        when(resultSet.next()).thenReturn(false);
        
        // Chiamata al metodo da testare
        OrdineSingolo result = ordineSingoloIDS.doRetrieveById(1);
        Mockito.verify(preparedStatement, times(1)).setInt(1, 1);

        assertNull(result);
    }

}
