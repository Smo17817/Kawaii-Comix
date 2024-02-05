package acquistoManagement;

import catalogoManagement.Prodotto;
import catalogoManagement.ProdottoIDS;
import catalogoManagement.ProdottoIDSTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import utenteManagement.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.logging.Level;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class CarrelloIDSTest {

    private DataSource ds;
    private CarrelloIDS carrelloIDS;
    private Connection connection;


    @BeforeEach
    public void setUp() throws Exception {

        ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection())
                .thenReturn(connection = mock(Connection.class));
        carrelloIDS = new CarrelloIDS(ds);
    }


    @Test
    @DisplayName("TCU6_1_1 doRetrieveCarrelloTest- Carrello Recuperato")
    public void doRetrieveCarrelloTest() throws  Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        User user = new User(1, "mariorossi@gmail.com", "Prova123", "Mario", "Rossi", "Via Roma 1", "Salerno", "84100", "SA", "Italia");
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);


        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getInt("id")).thenReturn(1);

        Carrello carrello = carrelloIDS.doRetrieveCarrello(user.getId());

        assertNotNull(carrello);

        assertEquals(1, carrello.getCarrelloId());

        Mockito.verify(preparedStatement, times(1)).setInt(1, user.getId());
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(1)).next();
        Mockito.verify(resultSet, times(1)).getInt("id");
    }

    @Test
    @DisplayName("TCU6_1_2 doRetrieveCarrelloTest_2- Carrello Generato e Recuperato")
    public void doRetrieveCarrelloTest_2() throws  Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        User user = new User(1, "mariorossi@gmail.com", "Prova123", "Mario", "Rossi", "Via Roma 1", "Salerno", "84100", "SA", "Italia");
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);


        Mockito.when(resultSet.next()).thenReturn(false);
        Mockito.when(resultSet.getInt("id")).thenReturn(1);

        Carrello carrello = carrelloIDS.doRetrieveCarrello(user.getId());

        assertNotNull(carrello);
        assertEquals(1, carrello.getCarrelloId());

        Mockito.verify(preparedStatement, times(2)).setInt(1, user.getId());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
        Mockito.verify(preparedStatement, times(2)).executeQuery();
        Mockito.verify(resultSet, times(1)).next();
        Mockito.verify(resultSet, times(1)).getInt("id");
    }
    
    @Test
    @DisplayName("TCU6_2_1 doRetrieveProdottiCarrelloTest")
    public void  doRetrieveProdottiCarrelloTest() throws Exception{
        PreparedStatement preparedStatement=mock(PreparedStatement.class);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        ProdottoIDS prodottoIDS = Mockito.mock(ProdottoIDS.class);
    	
        // Configurazione dello stubbing per il mock ProdottoIDS
    	Prodotto prodotto = mock(Prodotto.class);
    	
        when(prodotto.getIsbn()).thenReturn("12345678901234567");

        ResultSet resultSet = Mockito.mock(ResultSet.class);
        String isbnMock = prodotto.getIsbn();
        when(prodottoIDS.doRetrieveByIsbn(prodotto.getIsbn())).thenReturn(prodotto);

        
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Mockito.when(resultSet.next()).thenReturn(true, false);
        Mockito.when(resultSet.getString("prodotto_isbn")).thenReturn(isbnMock);
        Mockito.when(prodotto.getNome()).thenReturn("One piece");
        Mockito.when(prodotto.getAutore()).thenReturn("Eiichiro Oda");
        Mockito.when(prodotto.getDescrizione()).thenReturn("Mugiwara");
        Mockito.when(prodotto.getImmagine()).thenReturn("davidoneNudo.jpeg");
        Mockito.when(prodotto.getGenere()).thenReturn("Avventura");
        Mockito.when(prodotto.getCategoria()).thenReturn("Shonen");
        Mockito.when(prodotto.getPrezzo()).thenReturn(10.5);
        Mockito.when(prodotto.getQuantita()).thenReturn(20);
        Mockito.when(prodotto.getCopieVendute()).thenReturn(5);

        Carrello carrello = new Carrello(1);

        carrelloIDS.doRetrieveProdottiCarrello(carrello);

        System.out.println(carrello.getListaProdotti());

        assertTrue(!carrello.getListaProdotti().isEmpty());

    }
    
    //TODO Caso opposto retrieveProdottiCarrello
    
    @Test
    @DisplayName("TCU6_3_1 doDeleteProdottiCarrelloTest")
    public void doDeleteProdottiCarrelloTest() throws  Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);


        Carrello carrello = new Carrello(1);
        HashSet<Prodotto> listaProdotti = new HashSet<>(Arrays.asList(
                new Prodotto("10000000000000016", "One Piece 2", "Eiichiro Oda", "Rufy e i Cappelli di Paglia si dirigono verso il Grand Line, una pericolosa zona piena di avventure e segreti. Durante il loro viaggio, si imbattono in nuovi alleati e nemici.", "./images/op2.jpg", 5.45, 45, "Avventura", "Shonen", 10),
                new Prodotto("10000000000000010", "Dragon Ball Completo", "Akira Toriyama", "La storia inizia con il giovane Goku, un ragazzo dotato di forza sovrumana e una coda di scimmia. Goku incontra Bulma, una brillante scienziata, e insieme intraprendono la ricerca delle sette sfere del drago, oggetti magici che, quando riuniti, permettono di evocare il Drago Shenron, in grado di esaudire un desiderio.", "./images/dbCompleto.jpg", 135.99, 3, "Combattimento", "Shonen", 20)
        ));
        carrello.setListaProdotti(listaProdotti);

        carrelloIDS.doDeleteProdottiCarrello(carrello);

        Mockito.verify(preparedStatement, times(2)).setString(eq(1), anyString());
        Mockito.verify(preparedStatement, times(2)).executeUpdate();
    }
    
    
    @Test
    @DisplayName("TCU6_4_1 doCreateCarrelloTest- Carrello Creato")
    public  void doCreateCarrelloTest() throws Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        User user = new User(1, "mariorossi@gmail.com", "Prova123", "Mario", "Rossi", "Via Roma 1", "Salerno", "84100", "SA", "Italia");

        assertTrue(carrelloIDS.doCreateCarrello(user.getId()));

        Mockito.verify(preparedStatement, times(1)).setInt(1, user.getId());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    @DisplayName("6_4_2 doCreateCarrelloTest- Carrello Non Creato")
    public  void doNotCreateCarrelloTest() throws Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(preparedStatement.executeUpdate()).thenReturn(0);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        User user = new User(1, "mariorossi@gmail.com", "Prova123", "Mario", "Rossi", "Via Roma 1", "Salerno", "84100", "SA", "Italia");

        assertFalse(carrelloIDS.doCreateCarrello(user.getId()));

        Mockito.verify(preparedStatement, times(1)).setInt(1, user.getId());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    @DisplayName("TCU6_5_1 doDeleteCarrelloTest- Carrello Cancellato")
    public  void doDeleteCarrelloTest() throws Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);


        Carrello carrello = new Carrello(1);
        assertTrue(carrelloIDS.doDeleteCarrello(carrello.getCarrelloId()));

        Mockito.verify(preparedStatement, times(1)).setInt(1, carrello.getCarrelloId());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    @DisplayName("TCU6_5_2 doNotDeleteCarrelloTest- Carrello Non Cancellato")
    public  void doNotDeleteCarrelloTest() throws Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(preparedStatement.executeUpdate()).thenReturn(0);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);


        Carrello carrello = new Carrello(1);
        assertFalse(carrelloIDS.doDeleteCarrello(carrello.getCarrelloId()));

        Mockito.verify(preparedStatement, times(1)).setInt(1, carrello.getCarrelloId());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
    }
    
    @Test
    @DisplayName("TCU6_6_1 doSvuotaCarrelloTest- Carrello Svuotato")
    public  void doSvuotaCarrelloTest() throws Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);


        Carrello carrello = new Carrello(1);
        HashSet<Prodotto> listaProdotti = new HashSet<>(Arrays.asList(
                new Prodotto("10000000000000016", "One Piece 2", "Eiichiro Oda", "Rufy e i Cappelli di Paglia si dirigono verso il Grand Line, una pericolosa zona piena di avventure e segreti. Durante il loro viaggio, si imbattono in nuovi alleati e nemici.", "./images/op2.jpg", 5.45, 45, "Avventura", "Shonen", 10),
                new Prodotto("10000000000000010", "Dragon Ball Completo", "Akira Toriyama", "La storia inizia con il giovane Goku, un ragazzo dotato di forza sovrumana e una coda di scimmia. Goku incontra Bulma, una brillante scienziata, e insieme intraprendono la ricerca delle sette sfere del drago, oggetti magici che, quando riuniti, permettono di evocare il Drago Shenron, in grado di esaudire un desiderio.", "./images/dbCompleto.jpg", 135.99, 3, "Combattimento", "Shonen", 20)
        ));
        carrello.setListaProdotti(listaProdotti);

        carrelloIDS.doSvuotaCarrello(carrello);

        assertTrue(carrello.getListaProdotti().isEmpty());

        Mockito.verify(preparedStatement, times(2)).setInt(1, carrello.getCarrelloId());
        Mockito.verify(preparedStatement, times(2)).setString(eq(2), anyString());
        Mockito.verify(preparedStatement, times(2)).executeUpdate();


    }

}
