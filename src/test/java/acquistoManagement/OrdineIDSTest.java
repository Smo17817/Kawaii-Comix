package acquistoManagement;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.opentest4j.ArgumentsAreDifferent;

import catalogoManagement.Prodotto;
import utenteManagement.PasswordUtils;
import utenteManagement.User;

public class OrdineIDSTest {



    private OrdineIDS ordineIDS;
    private Prodotto prodotto;
    private OrdineSingolo ordineSingolo;
    private DataSource dataSource;
    private Connection connection;
    private PreparedStatement preparedStatement;

    private OrdineSingoloIDS mockordineSingoloIDS;

    @BeforeEach
    void setUp() throws SQLException {
        // Creazione dei mock
        dataSource = mock(DataSource.class);
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        // Configurazione del comportamento dei mock
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        // Inizializzazione della classe da testare con i mock
        ordineIDS = new OrdineIDS(dataSource);
        mockordineSingoloIDS = mock(OrdineSingoloIDS.class);
        ordineSingolo = mock(OrdineSingolo.class);
        prodotto = mock(Prodotto.class);
    }

    @Test
    @DisplayName("TCU doSaveOrdine - Salva")
    public void testDoSaveOrdineSalva() throws Exception {
        // Mock della classe OrdineSingolo

        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);

        // Creazione di un oggetto Ordine da testare
        Ordine ordine = new Ordine(1, new Date(System.currentTimeMillis()), 100.0, 1, 1, 1);
        ordine.setOrdiniSingoli(new ArrayList<>(List.of(ordineSingolo)));


        Field field = OrdineIDS.class.getDeclaredField("ordineSingoloIDS");
        field.setAccessible(true);
        field.set(ordineIDS, mockordineSingoloIDS);

        when(mockordineSingoloIDS.doSaveOrdineSingolo(ordineSingolo)).thenReturn(null);

        assertTrue(ordineIDS.doSaveOrdine(ordine));

        // Verifica delle chiamate ai metodi di mock
        verify(connection, times(1)).prepareStatement(anyString());//viene eseguito due volte , una volta per OrdineIDS, un'altra volta per OrdineSingolo
        verify(preparedStatement, times(1)).setInt(1, ordine.getId());
        verify(preparedStatement, times(1)).setDate(eq(2), any(java.sql.Date.class));
        verify(preparedStatement, times(1)).setDouble(3, ordine.getTotale());
        verify(preparedStatement, times(1)).setInt(4, ordine.getUserId());
        verify(preparedStatement, times(1)).setInt(5, ordine.getStato());
        verify(preparedStatement, times(1)).setInt(6, ordine.getMetodoSpedizione());
        verify(preparedStatement, times(1)).executeUpdate();//viene eseguito due volte , una volta per OrdineIDS, un'altra volta per OrdineSingolo
    }
    
    @Test
    @DisplayName("TCU doSaveOrdine - NonSalva")
    public void testDoSaveOrdineNonSalva() throws SQLException {
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(0);
        // Creazione di un oggetto Ordine da testare
        Ordine ordine = new Ordine(1, new Date(System.currentTimeMillis()), 100.0, 1, 1, 1);
        ordine.setOrdiniSingoli(new ArrayList<>(List.of(ordineSingolo)));

        assertFalse(ordineIDS.doSaveOrdine(ordine));

        // Verifica delle chiamate ai metodi di mock
        verify(connection, times(1)).prepareStatement(anyString());//viene eseguito due volte , una volta per OrdineIDS, un'altra volta per OrdineSingolo
        verify(preparedStatement, times(1)).setInt(1, ordine.getId());
        verify(preparedStatement, times(1)).setDate(eq(2), any(java.sql.Date.class));
        verify(preparedStatement, times(1)).setDouble(3, ordine.getTotale());
        verify(preparedStatement, times(1)).setInt(4, ordine.getUserId());
        verify(preparedStatement, times(1)).setInt(5, ordine.getStato());
        verify(preparedStatement, times(1)).setInt(6, ordine.getMetodoSpedizione());
        verify(preparedStatement, times(1)).executeUpdate();//viene eseguito due volte , una volta per OrdineIDS, un'altra volta per OrdineSingolo
    }
    
    @Test
    @DisplayName("TCU doDeleteOrdine - Elimina")
    public void testDoDeleteOrdineElimina() throws SQLException {
    	when(preparedStatement.executeUpdate()).thenReturn(1);
    	
    	assertTrue(ordineIDS.doDeleteOrdine(2));
        verify(connection, times(1)).prepareStatement(anyString());
        verify(preparedStatement, times(1)).setInt(1,2);
        verify(preparedStatement, times(1)).executeUpdate();
    }
    
    @Test
    @DisplayName("TCU doDeleteOrdine - NonElimina")
    public void testDoDeleteOrdineNonElimina() throws SQLException {
    	when(preparedStatement.executeUpdate()).thenReturn(0);

    	assertFalse(ordineIDS.doDeleteOrdine(2));
    	
        verify(connection, times(1)).prepareStatement(anyString());
        verify(preparedStatement, times(1)).setInt(1,2);
        verify(preparedStatement, times(1)).executeUpdate();
    }
    
    @Test
    @DisplayName("doUpdateStatoOrdine - Aggiorna")
    public void  testDoUpdateAggiorna() throws SQLException{
    	// Creazione di un oggetto Ordine da testare
        // Verifica delle chiamate ai metodi di mock
        Ordine ordine = new Ordine(1, new Date(System.currentTimeMillis()), 100.0, 1, 1, 1);
        java.sql.Date sqlDate = new java.sql.Date(ordine.getJavaDate().getTime());
    	when(preparedStatement.executeUpdate()).thenReturn(1);

        // Chiamo il metodo da testare
        boolean result = ordineIDS.doUpdateOrdine(ordine);

        // Verifica delle chiamate ai metodi di mock
        verify(connection, times(1)).prepareStatement(anyString());
        verify(preparedStatement, times(1)).setDate(1, sqlDate);
        verify(preparedStatement, times(1)).setDouble(2, ordine.getTotale());
        verify(preparedStatement, times(1)).setInt(3, ordine.getUserId());
        verify(preparedStatement, times(1)).setInt(4, ordine.getStato());
        verify(preparedStatement, times(1)).setInt(5, ordine.getMetodoSpedizione());
        verify(preparedStatement, times(1)).setInt(6, ordine.getId());
        verify(preparedStatement, times(1)).executeUpdate();

        // Verifica del risultato del metodo
        assertTrue(result);
    }
    
    @Test
    @DisplayName("doUpdateStatoOrdine - Ordine non trovato")
    public void  testDoUpdateOrdineNonTrovato() throws SQLException{
        // Creazione di un oggetto Ordine da testare
        // Verifica delle chiamate ai metodi di mock
        Ordine ordine = new Ordine(1, new Date(System.currentTimeMillis()), 100.0, 1, 1, 1);
        java.sql.Date sqlDate = new java.sql.Date(ordine.getJavaDate().getTime());
        when(preparedStatement.executeUpdate()).thenReturn(0);

        // Chiamo il metodo da testare
        boolean result = ordineIDS.doUpdateOrdine(ordine);

        // Verifica delle chiamate ai metodi di mock
        verify(connection, times(1)).prepareStatement(anyString());
        verify(preparedStatement, times(1)).setDate(1, sqlDate);
        verify(preparedStatement, times(1)).setDouble(2, ordine.getTotale());
        verify(preparedStatement, times(1)).setInt(3, ordine.getUserId());
        verify(preparedStatement, times(1)).setInt(4, ordine.getStato());
        verify(preparedStatement, times(1)).setInt(5, ordine.getMetodoSpedizione());
        verify(preparedStatement, times(1)).setInt(6, ordine.getId());
        verify(preparedStatement, times(1)).executeUpdate();

        // Verifica del risultato del metodo
        assertFalse(result);
    }
    
    @Test
    @DisplayName("doUpdateStatoById - Ordine trovato")
    public void  testDoUpdateOrdineById() throws SQLException{

        // Verifica delle chiamate ai metodi di mock
    	when(preparedStatement.executeUpdate()).thenReturn(1);

        // Chiamo il metodo da testare
        boolean result = ordineIDS.doUpdateStatoById(1,2);

        // Verifica delle chiamate ai metodi di mock
        verify(connection, times(1)).prepareStatement(anyString());
        verify(preparedStatement, times(1)).setInt(1, 2);
        verify(preparedStatement, times(1)).setInt(2, 1);
        verify(preparedStatement, times(1)).executeUpdate();

        // Verifica del risultato del metodo
        assertTrue(result);
    }
    
    @Test
    @DisplayName("doUpdateStatoById - Ordine Non trovato")
    public void  testDoUpdateOrdineByIdNonTrovato() throws SQLException{
        // Verifica delle chiamate ai metodi di mock
    	when(preparedStatement.executeUpdate()).thenReturn(0);

        // Chiamo il metodo da testare
        boolean result = ordineIDS.doUpdateStatoById(1,2);

        // Verifica delle chiamate ai metodi di mock
        verify(connection, times(1)).prepareStatement(anyString());
        verify(preparedStatement, times(1)).setInt(1, 2);
        verify(preparedStatement, times(1)).setInt(2, 1);
        verify(preparedStatement, times(1)).executeUpdate();

        // Verifica del risultato del metodo
        assertFalse(result);
    }

    @Test
    @DisplayName("TCU doRetrieveAllOrdiniTest")
    public void doRetrieveAllOrdiniTest() throws Exception {
        // Mock del ResultSet
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Impostazione del campo ordineSingoloIDS di ordineIDS con il mock mockordineSingoloIDS utilizzando la reflection
        Field field = OrdineIDS.class.getDeclaredField("ordineSingoloIDS");
        field.setAccessible(true);
        field.set(ordineIDS, mockordineSingoloIDS);



        OrdineSingolo ordineSingolo1 = new OrdineSingolo(1, 23 , 47.0 , 1, prodotto);
        OrdineSingolo ordineSingolo2 = new OrdineSingolo(2, 23 , 47.0 , 2, prodotto);

        // Mock dell'insieme di OrdineSingolo da restituire dal mock di OrdineSingoloIDS
        Collection<OrdineSingolo> ordiniSingoli = Arrays.asList(ordineSingolo1);
        Collection<OrdineSingolo> ordiniSingoli2 = Arrays.asList(ordineSingolo2);

        // Mock della chiamata a doRetrieveAllOrdiniSingoli
        Mockito.when(mockordineSingoloIDS.doRetrieveAllByOrdineId(1)).thenReturn(ordiniSingoli);
        Mockito.when(mockordineSingoloIDS.doRetrieveAllByOrdineId(2)).thenReturn(ordiniSingoli2);

        // Mock dei valori restituiti dal ResultSet
        Mockito.when(resultSet.next()).thenReturn(true,true, false);
        Mockito.when(resultSet.getInt("id")).thenReturn(1,2);
        Mockito.when(resultSet.getDate("data")).thenReturn((new java.sql.Date(new Date().getTime())),(new java.sql.Date(new Date().getTime())));
        Mockito.when(resultSet.getDouble("totale")).thenReturn(100.0, 105.0);
        Mockito.when(resultSet.getInt("site_user_id")).thenReturn(1, 2);
        Mockito.when(resultSet.getInt("stato_ordine_id")).thenReturn(1, 1);
        Mockito.when(resultSet.getInt("metodo_spedizione_id")).thenReturn(1, 1);

        // Esecuzione del metodo da testare
        Collection<Ordine> result = ordineIDS.doRetrieveAllOrdini();

        // Verifica del risultato
        assertEquals(2, result.size());

        // Verifica delle chiamate ai metodi del PreparedStatement
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(3)).next();
        Mockito.verify(resultSet, times(2)).getInt("id");
        Mockito.verify(resultSet, times(2)).getDate("data");
        Mockito.verify(resultSet, times(2)).getDouble("totale");
        Mockito.verify(resultSet, times(2)).getInt("site_user_id");
        Mockito.verify(resultSet, times(2)).getInt("stato_ordine_id");
        Mockito.verify(resultSet, times(2)).getInt("metodo_spedizione_id");

    }


    @Test
    @DisplayName("TCU doRetrieveOrdineByIdTest")
    public void doRetrieveOrdineByIdTest() throws Exception {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        java.sql.Date data = new java.sql.Date(new Date().getTime());
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getDate("data")).thenReturn(data);
        Mockito.when(resultSet.getDouble("totale")).thenReturn(15.99);
        Mockito.when(resultSet.getInt("site_user_id")).thenReturn(1);
        Mockito.when(resultSet.getInt("stato_ordine_id")).thenReturn(1);
        Mockito.when(resultSet.getInt("metodo_spedizione_id")).thenReturn(1);

        Ordine result = ordineIDS.doRetrieveById(1);

        

        assertNotNull(result);

        assertEquals(1 , result.getId());
        assertEquals(data , result.getJavaDate());
        assertEquals(15.99, result.getTotale());
        assertEquals(1, result.getUserId());
        assertEquals(1, result.getMetodoSpedizione());
        
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(preparedStatement, times(1)).setInt(1, 1);
        Mockito.verify(resultSet, times(1)).next();
        Mockito.verify(resultSet, times(1)).getDate("data");
        Mockito.verify(resultSet, times(1)).getDouble("totale");
        Mockito.verify(resultSet, times(1)).getInt("site_user_id");
        Mockito.verify(resultSet, times(1)).getInt("stato_ordine_id");
        Mockito.verify(resultSet, times(1)).getInt("metodo_spedizione_id");
    }
    
    @Test
    @DisplayName("TCU doRetrieveOrdineByIdTest - ordineId Non Trovato")
    public void doRetrieveOrdineByIdTestNonTrovato() throws Exception {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Mockito.when(resultSet.next()).thenReturn(false);

        Ordine result = ordineIDS.doRetrieveById(1);
        

        assertNull(result);
        
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(preparedStatement, times(1)).setInt(1, 1);
        Mockito.verify(resultSet, times(1)).next();
        
    }
    
    @Test
    @DisplayName("TCU doRetrieveOrdiniByUserIdTest")
    public void doRetrieveOrdiniByUserIdTest() throws Exception {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Field field = OrdineIDS.class.getDeclaredField("ordineSingoloIDS");
        field.setAccessible(true);
        field.set(ordineIDS, mockordineSingoloIDS);

        OrdineSingolo ordineSingolo1 = new OrdineSingolo(1, 23 , 47.0 , 1, prodotto);
        OrdineSingolo ordineSingolo2 = new OrdineSingolo(2, 23 , 47.0 , 2, prodotto);

        Collection<OrdineSingolo> ordiniSingoli = Arrays.asList(ordineSingolo1);
        Collection<OrdineSingolo> ordiniSingoli2 = Arrays.asList(ordineSingolo2);

        Mockito.when(mockordineSingoloIDS.doRetrieveAllByOrdineId(1)).thenReturn(ordiniSingoli);
        Mockito.when(mockordineSingoloIDS.doRetrieveAllByOrdineId(2)).thenReturn(ordiniSingoli2);

        Mockito.when(resultSet.next()).thenReturn(true, true, false);
        Mockito.when(resultSet.getInt("id")).thenReturn(1, 2);
        Mockito.when(resultSet.getDate("data")).thenReturn(new java.sql.Date(new Date().getTime()),new java.sql.Date(new Date().getTime()));
        Mockito.when(resultSet.getDouble("totale")).thenReturn(15.99, 14.01);
        Mockito.when(resultSet.getInt("site_user_id")).thenReturn(1, 2);
        Mockito.when(resultSet.getInt("stato_ordine_id")).thenReturn(1, 1);
        Mockito.when(resultSet.getInt("metodo_spedizione_id")).thenReturn(1, 1);

        Collection<Ordine> result = ordineIDS.doRetrieveByUserId(1);
        

        assertEquals(2, result.size());
        
        Mockito.verify(preparedStatement, times(1)).executeQuery();//si esegue 1 volta in Ordine e 2 in OrdineSingolo
        Mockito.verify(preparedStatement, times(1)).setInt(1, 1);
        Mockito.verify(resultSet, times(3)).next();// si esegue 3 volte in Ordine e 2 volte in OrdineSingolo
        Mockito.verify(resultSet, times(2)).getInt("id"); // si esegue una volta in Ordine e una in OrdineSingolo
        Mockito.verify(resultSet, times(2)).getDate("data");
        Mockito.verify(resultSet, times(2)).getDouble("totale");
        Mockito.verify(resultSet, times(2)).getInt("site_user_id");
        Mockito.verify(resultSet, times(2)).getInt("stato_ordine_id");
        Mockito.verify(resultSet, times(2)).getInt("metodo_spedizione_id");
    }

    @Test
    @DisplayName("TCU doRetrieveOrdiniByUserIdTest-Ordine Non Trovatp")
    public void doRetrieveOrdiniByUserIdTest_NotFound() throws Exception {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn( false);


        Collection<Ordine> result = ordineIDS.doRetrieveByUserId(1);


        assertEquals(0, result.size());

        Mockito.verify(preparedStatement, times(1)).executeQuery();//si esegue 1 volta in Ordine e 2 in OrdineSingolo
        Mockito.verify(preparedStatement, times(1)).setInt(1, 1);
        Mockito.verify(resultSet, times(1)).next();// si esegue 3 volte in Ordine e 2 volte in OrdineSingolo

    }


}
