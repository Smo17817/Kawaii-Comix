package acquistoManagement;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.exceptions.verification.opentest4j.ArgumentsAreDifferent;

import catalogoManagement.Prodotto;

public class OrdineIDSTest {

    

    private OrdineIDS ordineIDS;
    private Prodotto prodotto;
    private OrdineSingolo ordineSingolo;
    private DataSource dataSource;
    private Connection connection;
    private PreparedStatement preparedStatement;

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
        ordineSingolo = mock(OrdineSingolo.class);
        prodotto = mock(Prodotto.class);
    }

    @Test
    @DisplayName("TCU doSaveOrdine - Salva")
    void testDoSaveOrdineSalva() throws SQLException {
        // Mock della classe OrdineSingolo
        
        
        // Creazione di un oggetto Ordine da testare
        Ordine ordine = new Ordine(1, new Date(System.currentTimeMillis()), 100.0, 1, 1, 1);
        ordine.setOrdiniSingoli(new ArrayList<>(List.of(ordineSingolo)));
        when(ordineSingolo.getProdotto()).thenReturn(prodotto);
        when(prodotto.getIsbn()).thenReturn("12345678901234567");

        ordineIDS.doSaveOrdine(ordine);

        // Verifica delle chiamate ai metodi di mock
        verify(connection, times(2)).prepareStatement(anyString());//viene eseguito due volte , una volta per OrdineIDS, un'altra volta per OrdineSingolo
        verify(preparedStatement, times(1)).setInt(1, ordine.getId());
        verify(preparedStatement, times(1)).setDate(eq(2), any(java.sql.Date.class));
        verify(preparedStatement, times(1)).setDouble(3, ordine.getTotale());
        verify(preparedStatement, times(1)).setInt(4, ordine.getUserId());
        verify(preparedStatement, times(1)).setInt(5, ordine.getStato());
        verify(preparedStatement, times(1)).setInt(6, ordine.getMetodoSpedizione());
        verify(preparedStatement, times(2)).executeUpdate();//viene eseguito due volte , una volta per OrdineIDS, un'altra volta per OrdineSingolo
    }
    
    @Test
    @DisplayName("TCU doSaveOrdine - NonSalva")
    void testDoSaveOrdineNonSalva() throws SQLException {
        // Mock della classe OrdineSingolo
        boolean flag=false;
        try {
        // Creazione di un oggetto Ordine da testare
        Ordine ordine = new Ordine(1, new Date(System.currentTimeMillis()), 100.0, 1, 1, 1);
        ordine.setOrdiniSingoli(new ArrayList<>(List.of(ordineSingolo)));
        when(ordineSingolo.getProdotto()).thenReturn(prodotto);
        when(prodotto.getIsbn()).thenReturn("12345678901234567");

        ordineIDS.doSaveOrdine(ordine);

        // Verifica delle chiamate ai metodi di mock
        verify(connection, times(2)).prepareStatement(anyString());//viene eseguito due volte , una volta per OrdineIDS, un'altra volta per OrdineSingolo
        verify(preparedStatement, times(1)).setInt(1, ordine.getId());
        verify(preparedStatement, times(1)).setDate(eq(3), any(java.sql.Date.class));
        verify(preparedStatement, times(1)).setDouble(2, ordine.getTotale());
        verify(preparedStatement, times(1)).setInt(4, ordine.getUserId());
        verify(preparedStatement, times(1)).setInt(5, ordine.getStato());
        verify(preparedStatement, times(1)).setInt(6, ordine.getMetodoSpedizione());
        verify(preparedStatement, times(2)).executeUpdate();//viene eseguito due volte , una volta per OrdineIDS, un'altra volta per OrdineSingolo
        
        }catch(ArgumentsAreDifferent e) {
        	flag = true;
        }
        
        assertEquals(true, flag);

    }
    
    @Test
    @DisplayName("TCU doDeleteOrdine - Elimina")
    void testDoDeleteOrdineElimina() throws SQLException {
    	
    	
    	when(preparedStatement.executeUpdate()).thenReturn(1);
    	
    	boolean result = ordineIDS.doDeleteOrdine(2);
        verify(connection, times(1)).prepareStatement(anyString());
        verify(preparedStatement, times(1)).setInt(1,2);
        verify(preparedStatement, times(1)).executeUpdate();

        assertTrue(result);
    	
    }
    
    @Test
    @DisplayName("TCU doDeleteOrdine - NonElimina")
    void testDoDeleteOrdineNonElimina() throws SQLException {
    	
    	ordineIDS.doDeleteOrdine(4);
    	try {
        verify(connection, times(1)).prepareStatement(anyString());
        verify(preparedStatement, times(1)).setInt(1,2);
        verify(preparedStatement, times(1)).executeUpdate();

    	}
    	catch(ArgumentsAreDifferent e) {
    		   		
    	}
    	
    }
    
    @Test
    @DisplayName("doUpdateStatoOrdine - Aggiorna")
    void  testDoUpdateAggiorna() throws SQLException{
    	
    	// Creazione di un oggetto Ordine da testare
        // Verifica delle chiamate ai metodi di mock
        Ordine ordine = new Ordine(1, new Date(System.currentTimeMillis()), 100.0, 1, 1, 1);
    	when(preparedStatement.executeUpdate()).thenReturn(1);

        // Chiamo il metodo da testare
        boolean result = ordineIDS.doUpdateOrdine(1, new Date(System.currentTimeMillis()), 150.0, 2, 2, 2);

        // Verifica delle chiamate ai metodi di mock
        verify(connection, times(1)).prepareStatement(anyString());
        verify(preparedStatement, times(1)).setDate(eq(1), any(java.sql.Date.class));
        verify(preparedStatement, times(1)).setDouble(2, 150.0);
        verify(preparedStatement, times(1)).setInt(3, 2);
        verify(preparedStatement, times(1)).setInt(4, 2);
        verify(preparedStatement, times(1)).setInt(5, 2);
        verify(preparedStatement, times(1)).setInt(6, 1);
        verify(preparedStatement, times(1)).executeUpdate();

        // Verifica del risultato del metodo
        assertTrue(result);
    }
    
    @Test
    @DisplayName("doUpdateStatoOrdine - NonAggiorna")
    void  testDoUpdateNonAggiorna() throws SQLException{
    	
    	// Creazione di un oggetto Ordine da testare
        // Verifica delle chiamate ai metodi di mock
        Ordine ordine = new Ordine(1, new Date(System.currentTimeMillis()), 100.0, 1, 1, 1);
    	when(preparedStatement.executeUpdate()).thenReturn(0);

        // Chiamo il metodo da testare
        boolean result = ordineIDS.doUpdateOrdine(1, new Date(System.currentTimeMillis()), 150.0, 2, 2, 2);

        // Verifica delle chiamate ai metodi di mock
        verify(connection, times(1)).prepareStatement(anyString());
        verify(preparedStatement, times(1)).setDate(eq(1), any(java.sql.Date.class));
        verify(preparedStatement, times(1)).setDouble(2, 150.0);
        verify(preparedStatement, times(1)).setInt(3, 2);
        verify(preparedStatement, times(1)).setInt(4, 2);
        verify(preparedStatement, times(1)).setInt(5, 2);
        verify(preparedStatement, times(1)).setInt(6, 1);
        verify(preparedStatement, times(1)).executeUpdate();

        // Verifica del risultato del metodo
        assertFalse(result);
    }
}
