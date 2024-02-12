package acquistoManagement;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
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
    }
    
    @Test
    @DisplayName("TCU7_1_1 doSaveOrdineSingoloTestSalva")
    public void doSaveOrdineSingoloTestSalva() throws Exception {

        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        OrdineSingolo ordineSingolo = new OrdineSingolo(2, 10.0, 1, prodotto);


        assertTrue(ordineSingoloIDS.doSaveOrdineSingolo(ordineSingolo));
        // Verifica che il metodo setString sia stato chiamato con i valori corretti
        Mockito.verify(preparedStatement, times(1)).setInt(1, ordineSingolo.getQuantita());
        Mockito.verify(preparedStatement, times(1)).setDouble(2, ordineSingolo.getTotParziale());
        Mockito.verify(preparedStatement, times(1)).setInt(3, ordineSingolo.getOrdineId());
        Mockito.verify(preparedStatement, times(1)).setString(4, ordineSingolo.getProdotto().getImmagine() );
        Mockito.verify(preparedStatement, times(1)).setString(5, ordineSingolo.getProdotto().getNome() );
        // Verifica che il metodo executeUpdate sia stato chiamato
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
        
    }
    
    @Test
    @DisplayName("TCU7_2_1 doDeleteOrdineSingolo- Ordine Singolo Cancellato")
    public void doDeleteOrdineSingoloTest() throws Exception{
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        OrdineSingolo ordineSingolo = new OrdineSingolo(1,2, 10.0, 1, prodotto);

        assertTrue(ordineSingoloIDS.doDeleteOrdineSingolo(1));

        Mockito.verify(preparedStatement, times(1)).setInt(1, ordineSingolo.getId());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
    }
    
    @Test
    @DisplayName("TCU7_2_2 doDeleteOrdineSingolo- Ordine Singolo Non Cancellato")
    public void doDeleteOrdineSingoloTestNonElimina() throws Exception{
        
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(0);
        OrdineSingolo ordineSingolo = new OrdineSingolo(1,2, 10.0, 1, prodotto);

        assertFalse(ordineSingoloIDS.doDeleteOrdineSingolo(1));

        Mockito.verify(preparedStatement, times(1)).setInt(1, ordineSingolo.getId());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();

    }
    
    @Test
    @DisplayName("TCU7_3_1 doUpdateStatoOrdine - Aggiorna")
    public void  testDoUpdateAggiorna() throws SQLException{
        OrdineSingolo ordineSingolo = new OrdineSingolo(1, 2 , 10.0 ,1, prodotto);
    	when(preparedStatement.executeUpdate()).thenReturn(1);

        // Chiamo il metodo da testare
        assertTrue(ordineSingoloIDS.doUpdateOrdineSingolo(ordineSingolo));

        // Verifica delle chiamate ai metodi di mock
        verify(connection, times(1)).prepareStatement(anyString());
        verify(preparedStatement, times(1)).setInt(1, ordineSingolo.getQuantita());
        verify(preparedStatement, times(1)).setDouble(2, ordineSingolo.getTotParziale());
        verify(preparedStatement, times(1)).setInt(3, ordineSingolo.getOrdineId());
        verify(preparedStatement, times(1)).setString(4, ordineSingolo.getProdotto().getImmagine());
        verify(preparedStatement, times(1)).setString(5, ordineSingolo.getProdotto().getNome());
        verify(preparedStatement,times(1)).setInt(6, ordineSingolo.getId());
        verify(preparedStatement, times(1)).executeUpdate();
    }
    
    @Test
    @DisplayName("TCU7_3_2 doUpdateStatoOrdine - NonAggiorna")
    public void  testDoUpdateNonAggiorna() throws SQLException{
        OrdineSingolo ordineSingolo = new OrdineSingolo(1, 2 , 10.0 ,1, prodotto);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        // Chiamo il metodo da testare
        assertFalse(ordineSingoloIDS.doUpdateOrdineSingolo(ordineSingolo));

        // Verifica delle chiamate ai metodi di mock
        verify(connection, times(1)).prepareStatement(anyString());
        verify(preparedStatement, times(1)).setInt(1, ordineSingolo.getQuantita());
        verify(preparedStatement, times(1)).setDouble(2, ordineSingolo.getTotParziale());
        verify(preparedStatement, times(1)).setInt(3, ordineSingolo.getOrdineId());
        verify(preparedStatement, times(1)).setString(4, ordineSingolo.getProdotto().getImmagine());
        verify(preparedStatement, times(1)).setString(5, ordineSingolo.getProdotto().getNome());
        verify(preparedStatement,times(1)).setInt(6, ordineSingolo.getId());
        verify(preparedStatement, times(1)).executeUpdate();
    }
    
    @Test
    @DisplayName("TCU7_3_1 doRetrieveAllOrdiniSingoliTest")
    public void doRetrieveAllOrdiniSingoliTest() throws Exception {
        
        
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        
        
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getInt("id")).thenReturn(1, 2);
        when(resultSet.getInt("quantità")).thenReturn(10, 11);
        when(resultSet.getDouble("totale_parziale")).thenReturn(10.5, 9.5);
        when(resultSet.getInt("ordini_id")).thenReturn(100, 112);
        when(resultSet.getString("immagine_prodotto")).thenReturn("immagine1", "immagine2");
        when(resultSet.getString("nome_prodotto")).thenReturn("nome1", "nome2");
        
        
        Collection<OrdineSingolo> result = ordineSingoloIDS.doRetrieveAllOrdineSingolo();
        assertEquals(2, result.size());

        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(3)).next();
        Mockito.verify(resultSet, times(2)).getInt("id");
        Mockito.verify(resultSet, times(2)).getInt("quantità");
        Mockito.verify(resultSet, times(2)).getDouble("totale_parziale");
        Mockito.verify(resultSet, times(2)).getInt("ordini_id");
        Mockito.verify(resultSet, times(2)).getString("immagine_prodotto");
        Mockito.verify(resultSet, times(2)).getString("nome_prodotto");

    }
    
    @Test
    @DisplayName("TCU7_5_1 doRetrieveAllOrdiniSingoliByOrdineId")
    public void testDoRetrieveAllByOrdineId() throws Exception {

    	
    	Prodotto prodotto1 = new Prodotto("nome1", "immagine1");
    	Prodotto prodotto2 = new Prodotto("nome2", "immagine2");
    	
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        

        OrdineSingolo ordineSingolo1 = new OrdineSingolo(22, 10, 10.5, 1, prodotto1);
        OrdineSingolo ordineSingolo2 = new OrdineSingolo(23, 10, 9.5, 1, prodotto2);

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getInt("id")).thenReturn(ordineSingolo1.getId(), ordineSingolo2.getId());
        when(resultSet.getInt("quantità")).thenReturn(ordineSingolo1.getQuantita(), ordineSingolo2.getQuantita());
        when(resultSet.getDouble("totale_parziale")).thenReturn(ordineSingolo1.getTotParziale(), ordineSingolo2.getTotParziale());
        when(resultSet.getInt("ordini_id")).thenReturn(ordineSingolo1.getOrdineId(), ordineSingolo2.getOrdineId());
        when(resultSet.getString("nome_prodotto")).thenReturn(ordineSingolo1.getProdotto().getNome(), ordineSingolo2.getProdotto().getNome());
        when(resultSet.getString("immagine_prodotto")).thenReturn(ordineSingolo1.getProdotto().getImmagine(), ordineSingolo2.getProdotto().getImmagine());

        Collection<OrdineSingolo> result = ordineSingoloIDS.doRetrieveAllByOrdineId(1);

        assertEquals(2, result.size()); // Verifica che ci sia un solo OrdineSingolo nel risultato

        for (OrdineSingolo ordineSingolo : result) {
            if (ordineSingolo.getId() == ordineSingolo1.getId()) {
                assertEquals(ordineSingolo1.getQuantita(), ordineSingolo.getQuantita());
                assertEquals(ordineSingolo1.getTotParziale(), ordineSingolo.getTotParziale());
                assertEquals(ordineSingolo1.getOrdineId(), ordineSingolo.getOrdineId());
                assertEquals(ordineSingolo1.getProdotto().getImmagine(), ordineSingolo.getProdotto().getImmagine());
                assertEquals(ordineSingolo1.getProdotto().getNome(), ordineSingolo.getProdotto().getNome());

            } else if (ordineSingolo.getId() == ordineSingolo2.getId()) {
                assertEquals(ordineSingolo2.getQuantita(), ordineSingolo.getQuantita());
                assertEquals(ordineSingolo2.getTotParziale(), ordineSingolo.getTotParziale());
                assertEquals(ordineSingolo2.getOrdineId(), ordineSingolo.getOrdineId());
                assertEquals(ordineSingolo2.getProdotto().getImmagine(), ordineSingolo.getProdotto().getImmagine());
                assertEquals(ordineSingolo2.getProdotto().getNome(), ordineSingolo.getProdotto().getNome());
            }
        }

        Mockito.verify(preparedStatement, times(1)).setInt(eq(1), anyInt());
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(3)).next();
        Mockito.verify(resultSet, times(2)).getInt("id");
        Mockito.verify(resultSet, times(2)).getInt("quantità");
        Mockito.verify(resultSet, times(2)).getDouble("totale_parziale");
        Mockito.verify(resultSet, times(2)).getString("immagine_prodotto");
        Mockito.verify(resultSet, times(2)).getString("nome_prodotto");

    }


    @Test
    @DisplayName("TCU7_6_1 doRetrieveOrdiniSingoliById")
    public void testDoRetrieveOrdineSingoloById() throws Exception {

    	Prodotto prodotto1 = new Prodotto("nome1", "immagine1");

        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        OrdineSingolo ordineSingolo = new OrdineSingolo(22, 10, 10.5, 1, prodotto1);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(ordineSingolo.getId());
        when(resultSet.getInt("quantità")).thenReturn(ordineSingolo.getQuantita());
        when(resultSet.getDouble("totale_parziale")).thenReturn(ordineSingolo.getTotParziale());
        when(resultSet.getInt("ordini_id")).thenReturn(ordineSingolo.getOrdineId());
        when(resultSet.getString("nome_prodotto")).thenReturn(ordineSingolo.getProdotto().getNome());
        when(resultSet.getString("immagine_prodotto")).thenReturn(ordineSingolo.getProdotto().getImmagine());

        
        OrdineSingolo result = ordineSingoloIDS.doRetrieveById(ordineSingolo.getId());

        assertNotNull(result);
        assertEquals(result.getId(), ordineSingolo.getId());
        assertEquals(result.getQuantita(), ordineSingolo.getQuantita());
        assertEquals(result.getOrdineId(), ordineSingolo.getOrdineId());
        assertEquals(result.getTotParziale(), ordineSingolo.getTotParziale());
        assertEquals(result.getProdotto(), ordineSingolo.getProdotto());

        verify(preparedStatement,times(1)).setInt(1, ordineSingolo.getId());
        verify(preparedStatement,times(1)).executeQuery();
        verify(resultSet,times(1)).next();
        verify(resultSet, times(1)).getInt("quantità");
        verify(resultSet, times(1)).getDouble("totale_parziale");
        verify(resultSet, times(1)).getInt("ordini_id");
        verify(resultSet, times(1)).getString("nome_prodotto");
        verify(resultSet, times(1)).getString("immagine_prodotto");
        
    }
    
    @Test
    @DisplayName("TCU7_6_2 doRetrieveOrdineSingoloById - Id non Trovato")
    void testDoRetrieveByIdNonTrovato() throws Exception {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Prodotto prodotto1 = Mockito.mock(Prodotto.class);
        OrdineSingolo ordineSingolo = new OrdineSingolo(22, 10, 10.5, 1, prodotto1);
        when(resultSet.next()).thenReturn(false);

        OrdineSingolo result = ordineSingoloIDS.doRetrieveById(ordineSingolo.getId());
        assertNull(result);

        verify(preparedStatement,times(1)).setInt(1, ordineSingolo.getId());
        verify(preparedStatement,times(1)).executeQuery();
        verify(resultSet,times(1)).next();
    }

}
