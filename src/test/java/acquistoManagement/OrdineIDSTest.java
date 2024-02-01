package acquistoManagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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

public class OrdineIDSTest {

    private DataSource ds;
    private Connection connection;
    private OrdineIDS ordineIDS;

    @BeforeEach
    public void setUp() throws Exception {
        ds = mock(DataSource.class);
        when(ds.getConnection()).thenReturn(connection = mock(Connection.class));
        ordineIDS = new OrdineIDS(ds);
    }

    @Test
    @DisplayName("doSaveOrdineTest")
    public void doSaveOrdineTest() throws Exception {
        // Mock del preparedStatement
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        // Configura il mock per ritornare il preparedStatement quando il metodo prepareStatement viene chiamato sulla connessione
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);

        // Creazione di un oggetto Ordine da testare
        Ordine ordine = new Ordine();
        ordine.setId(1);
        ordine.setData(new Date()); // Supponiamo che getData() restituisca una stringa nel formato "yyyy-MM-dd"
        ordine.setTotale(100.0);
        ordine.setUserId(1);
        ordine.setStato(2);
        ordine.setMetodoSpedizione(3);

        OrdineSingoloIDS ordineSingoloIDS = mock(OrdineSingoloIDS.class);
        // Impostare i dettagli di ordineSingolo se necessario
        OrdineSingolo ordineSingolo = mock(OrdineSingolo.class);
        ArrayList<OrdineSingolo> ordiniSingoli = new ArrayList<>();
        when(ordineSingolo.getQuantita()).thenReturn(5); // Imposta un valore arbitrario
        ordiniSingoli.add(ordineSingolo);
        when(ordine.getOrdiniSingoli()).thenReturn(ordiniSingoli);

        ordine.setOrdiniSingoli(new ArrayList<>(Arrays.asList(ordineSingolo)));

        // Chiamo il metodo da testare
        ordineIDS.doSaveOrdine(ordine);

        // Verifica che il metodo setXXX sia stato chiamato con i valori corretti
        verify(preparedStatement, times(1)).setInt(eq(1), eq(1));
        verify(preparedStatement, times(1)).setDate(eq(2), any());
        verify(preparedStatement, times(1)).setDouble(eq(3), eq(100.0));
        verify(preparedStatement, times(1)).setInt(eq(4), eq(1));
        verify(preparedStatement, times(1)).setInt(eq(5), eq(2));
        verify(preparedStatement, times(1)).setInt(eq(6), eq(3));
        verify(preparedStatement, times(1)).executeUpdate();

        // Puoi anche verificare se doSaveOrdineSingoloAssociato è stato chiamato correttamente
        verify(ordineIDS, times(1)).doSaveOrdineSingoloAssociato(ordineSingolo);
    }
}
