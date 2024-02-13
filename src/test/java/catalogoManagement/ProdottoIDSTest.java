package catalogoManagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import utenteManagement.UserIDS;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class ProdottoIDSTest {

    private DataSource ds;
    private ProdottoIDS prodottoIDS;
    private Connection connection;



    @BeforeEach
    public void setUp() throws Exception {

        ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection())
                .thenReturn(connection = mock(Connection.class));
        prodottoIDS = new ProdottoIDS(ds);
    }


    @Test
    @DisplayName("TCU4_1_1 doSaveProdottoTest-Prodotto Salvato")
    public void doSaveProdottoTest() throws Exception {
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Prodotto prodotto = new Prodotto("98247387402384203", "Demon Slayer", "Antonio Taranto(Totò)", "L'avvincente storia di una bionda gigantesca che conquisterà il cuore del nostro protagonista", "./images/DemonSlayer", 104.00,00, "Crimine" , "Manga Italiani", 0);

        assertTrue(prodottoIDS.doSaveProdotto(prodotto));


        Mockito.verify(preparedStatement,times(1)).setString(1 , prodotto.getIsbn());
        Mockito.verify(preparedStatement,times(1)).setString(2 , prodotto.getNome());
        Mockito.verify(preparedStatement,times(1)).setString(3 , prodotto.getAutore());
        Mockito.verify(preparedStatement,times(1)).setString(4 , prodotto.getDescrizione());
        Mockito.verify(preparedStatement,times(1)).setString(5 , prodotto.getImmagine());
        Mockito.verify(preparedStatement,times(1)).setDouble(6 , prodotto.getPrezzo());
        Mockito.verify(preparedStatement,times(1)).setInt(7 , prodotto.getQuantita());
        Mockito.verify(preparedStatement,times(1)).setString(8 , prodotto.getGenere());
        Mockito.verify(preparedStatement,times(1)).setString(9 , prodotto.getCategoria());
        Mockito.verify(preparedStatement,times(1)).setInt(10, prodotto.getCopieVendute());

        Mockito.verify(preparedStatement,times(1)).executeUpdate();
    }

    
    @Test
    @DisplayName("TCU4_1_2 doSaveProdottoTest-Prodotto non salvato")
    public void doSaveProdottoTestNonSalva() throws Exception {
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(preparedStatement.executeUpdate()).thenReturn(0);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Prodotto prodotto = new Prodotto("98247387402384203", "Demon Slayer", "Antonio Taranto(Totò)", "L'avvincente storia di una bionda gigantesca che conquisterà il cuore del nostro protagonista", "./images/DemonSlayer", 104.00,00, "Crimine" , "Manga Italiani", 0);

        assertFalse(prodottoIDS.doSaveProdotto(prodotto));


        Mockito.verify(preparedStatement,times(1)).setString(1 , prodotto.getIsbn());
        Mockito.verify(preparedStatement,times(1)).setString(2 , prodotto.getNome());
        Mockito.verify(preparedStatement,times(1)).setString(3 , prodotto.getAutore());
        Mockito.verify(preparedStatement,times(1)).setString(4 , prodotto.getDescrizione());
        Mockito.verify(preparedStatement,times(1)).setString(5 , prodotto.getImmagine());
        Mockito.verify(preparedStatement,times(1)).setDouble(6 , prodotto.getPrezzo());
        Mockito.verify(preparedStatement,times(1)).setInt(7 , prodotto.getQuantita());
        Mockito.verify(preparedStatement,times(1)).setString(8 , prodotto.getGenere());
        Mockito.verify(preparedStatement,times(1)).setString(9 , prodotto.getCategoria());
        Mockito.verify(preparedStatement,times(1)).setInt(10, prodotto.getCopieVendute());

        Mockito.verify(preparedStatement,times(1)).executeUpdate();
    }
    
    @Test
    @DisplayName("TCU4_2_1 doDeleteProdottoTest-Prodotto eliminato")
    public void doDeleteProdottoTest() throws  Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Prodotto prodotto = new Prodotto("98247387402384203", "Demon Slayer", "Antonio Taranto(Totò)", "L'avvincente storia di una bionda gigantesca che conquisterà il cuore del nostro protagonista", "./images/DemonSlayer", 104.00,104, "Psicologico" , "Manga Italiani", 0);
        assertTrue(prodottoIDS.doDeleteProdotto(prodotto.getIsbn()));


        Mockito.verify(preparedStatement,times(1)).setString(1 , "98247387402384203");
        Mockito.verify(preparedStatement,times(1)).executeUpdate();
    }

    @Test
    @DisplayName("TCU4_2_2 doNotDeleteProdottoTest-Prodotto Non eliminato")
    public void doNotDeleteProdottoTest() throws  Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(preparedStatement.executeUpdate()).thenReturn(0);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Prodotto prodotto = new Prodotto("98247387402384203", "Demon Slayer", "Antonio Taranto(Totò)", "L'avvincente storia di una bionda gigantesca che conquisterà il cuore del nostro protagonista", "./images/DemonSlayer", 104.00,104, "Psicologico" , "Manga Italiani", 0);
        assertFalse(prodottoIDS.doDeleteProdotto(prodotto.getIsbn()));


        Mockito.verify(preparedStatement,times(1)).setString(1 , "98247387402384203");
        Mockito.verify(preparedStatement,times(1)).executeUpdate();
    }

    @Test
    @DisplayName("TCU4_3_1 doUpdateProdotto- Prodotto Aggiornato")
    public  void doUpdateProdottoTest() throws  Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);


        Prodotto prodotto = new Prodotto("98247387402384203", "Demon Slayer", "Antonio Taranto(Totò)", "L'avvincente storia di una bionda gigantesca che conquisterà il cuore del nostro protagonista", "./images/DemonSlayer", 104.00,104, "Psicologico" , "Manga Italiani", 0);

        assertTrue(prodottoIDS.doUpdateProdotto(prodotto));

        Mockito.verify(preparedStatement,times(1)).setString(1 , prodotto.getNome());
        Mockito.verify(preparedStatement,times(1)).setString(2 , prodotto.getAutore());
        Mockito.verify(preparedStatement,times(1)).setString(3 , prodotto.getDescrizione());
        Mockito.verify(preparedStatement,times(1)).setString(4 , prodotto.getImmagine());
        Mockito.verify(preparedStatement,times(1)).setDouble(5 , prodotto.getPrezzo());
        Mockito.verify(preparedStatement,times(1)).setInt(6 , prodotto.getQuantita());
        Mockito.verify(preparedStatement,times(1)).setString(7 , prodotto.getCategoria());
        Mockito.verify(preparedStatement,times(1)).setString(8 , prodotto.getGenere());
        Mockito.verify(preparedStatement,times(1)).setString(9, prodotto.getIsbn());

        Mockito.verify(preparedStatement,times(1)).executeUpdate();
    }

    @Test
    @DisplayName("TCU4_3_2 doNotUpdateProdotto- Prodotto Non Aggiornato")
    public  void doNotUpdateProdottoTest() throws  Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(preparedStatement.executeUpdate()).thenReturn(0);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);


        Prodotto prodotto = new Prodotto("98247387402384203", "Demon Slayer", "Antonio Taranto(Totò)", "L'avvincente storia di una bionda gigantesca che conquisterà il cuore del nostro protagonista", "./images/DemonSlayer", 104.00,104, "Psicologico" , "Manga Italiani", 0);

        assertFalse(prodottoIDS.doUpdateProdotto(prodotto));

        Mockito.verify(preparedStatement,times(1)).setString(1 , prodotto.getNome());
        Mockito.verify(preparedStatement,times(1)).setString(2 , prodotto.getAutore());
        Mockito.verify(preparedStatement,times(1)).setString(3 , prodotto.getDescrizione());
        Mockito.verify(preparedStatement,times(1)).setString(4 , prodotto.getImmagine());
        Mockito.verify(preparedStatement,times(1)).setDouble(5 , prodotto.getPrezzo());
        Mockito.verify(preparedStatement,times(1)).setInt(6 , prodotto.getQuantita());
        Mockito.verify(preparedStatement,times(1)).setString(7 , prodotto.getCategoria());
        Mockito.verify(preparedStatement,times(1)).setString(8 , prodotto.getGenere());
        Mockito.verify(preparedStatement,times(1)).setString(9, prodotto.getIsbn());

        Mockito.verify(preparedStatement,times(1)).executeUpdate();
    }

    @Test
    @DisplayName("TCU4_4_1 doRetrieveAllProdotti")
    public void doRetrieveAllProdottiTest() throws Exception {
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Mockito.when(resultSet.next()).thenReturn(true,true,false);
        Mockito.when(resultSet.getString("isbn")).thenReturn("10000000000000016", "10000000000000010");
        Mockito.when(resultSet.getString("nome")).thenReturn("One Piece 2", "Dragon Ball Completo");
        Mockito.when(resultSet.getString("autore")).thenReturn("Eiichiro Oda", "Akira Toriyama");
        Mockito.when(resultSet.getString("descrizione")).thenReturn("Rufy e i Cappelli di Paglia si dirigono verso il Grand Line, una pericolosa zona piena di avventure e segreti. Durante il loro viaggio, si imbattono in nuovi alleati e nemici.","La storia inizia con il giovane Goku, un ragazzo dotato di forza sovrumana e una coda di scimmia. Goku incontra Bulma, una brillante scienziata, e insieme intraprendono la ricerca delle sette sfere del drago, oggetti magici che, quando riuniti, permettono di evocare il Drago Shenron, in grado di esaudire un desiderio.");
        Mockito.when(resultSet.getString("immagine_prod")).thenReturn("./images/op2.jpg", "./images/dbCompleto.jpg");
        Mockito.when(resultSet.getDouble("prezzo")).thenReturn(5.45,135.99);
        Mockito.when(resultSet.getInt("quantita")).thenReturn(45 ,3);
        Mockito.when(resultSet.getString("genere_nome")).thenReturn("Avventura", "Combattimento");
        Mockito.when(resultSet.getString("categoria_nome")).thenReturn("Shonen", "Shonen");
        Mockito.when(resultSet.getInt("copie_vendute")).thenReturn(10, 20);

        Collection<Prodotto> result = prodottoIDS.doRetreiveAllProdotti();

        assertEquals(2, result.size());

        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(3)).next();
        Mockito.verify(resultSet, times(2)).getString("isbn");
        Mockito.verify(resultSet, times(2)).getString("nome");
        Mockito.verify(resultSet, times(2)).getString("autore");
        Mockito.verify(resultSet, times(2)).getString("descrizione");
        Mockito.verify(resultSet, times(2)).getString("immagine_prod");
        Mockito.verify(resultSet, times(2)).getDouble("prezzo");
        Mockito.verify(resultSet, times(2)).getInt("quantita");
        Mockito.verify(resultSet, times(2)).getString("genere_nome");
        Mockito.verify(resultSet, times(2)).getString("categoria_nome");
        Mockito.verify(resultSet, times(2)).getInt("copie_vendute");
    }


    @Test
    @DisplayName("TCU4_5_1 doRetrieveByIsbnTest- Prodotto Esistente")
    public void doRetrieveByIsbnTest() throws Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getString("nome")).thenReturn("One Piece 2");
        Mockito.when(resultSet.getString("autore")).thenReturn("Eiichiro Oda");
        Mockito.when(resultSet.getString("descrizione")).thenReturn("Rufy e i Cappelli di Paglia si dirigono verso il Grand Line, una pericolosa zona piena di avventure e segreti. Durante il loro viaggio, si imbattono in nuovi alleati e nemici.");
        Mockito.when(resultSet.getString("immagine_prod")).thenReturn("./images/op2.jpg");
        Mockito.when(resultSet.getDouble("prezzo")).thenReturn(5.45);
        Mockito.when(resultSet.getInt("quantita")).thenReturn(45);
        Mockito.when(resultSet.getString("genere_nome")).thenReturn("Avventura");
        Mockito.when(resultSet.getString("categoria_nome")).thenReturn("Shonen");
        Mockito.when(resultSet.getInt("copie_vendute")).thenReturn(10);

        Prodotto prodotto = prodottoIDS.doRetrieveByIsbn("10000000000000016");

        assertNotNull(prodotto);

        assertEquals("10000000000000016", prodotto.getIsbn());
        assertEquals("One Piece 2", prodotto.getNome());
        assertEquals("Eiichiro Oda", prodotto.getAutore());
        assertEquals("Rufy e i Cappelli di Paglia si dirigono verso il Grand Line, una pericolosa zona piena di avventure e segreti. Durante il loro viaggio, si imbattono in nuovi alleati e nemici.", prodotto.getDescrizione());
        assertEquals("./images/op2.jpg" , prodotto.getImmagine());
        assertEquals(Double.valueOf(5.45), prodotto.getPrezzo());
        assertEquals(Integer.valueOf(45) , prodotto.getQuantita());
        assertEquals("Avventura" , prodotto.getGenere());
        assertEquals("Shonen", prodotto.getCategoria());
        assertEquals(Integer.valueOf(10), prodotto.getCopieVendute());

        Mockito.verify(preparedStatement, times(1)).setString(1,"10000000000000016");
        Mockito.verify(resultSet, times(1)).next();
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(1)).getString("nome");
        Mockito.verify(resultSet, times(1)).getString("autore");
        Mockito.verify(resultSet, times(1)).getString("descrizione");
        Mockito.verify(resultSet, times(1)).getString("immagine_prod");
        Mockito.verify(resultSet, times(1)).getDouble("prezzo");
        Mockito.verify(resultSet, times(1)).getInt("quantita");
        Mockito.verify(resultSet, times(1)).getString("genere_nome");
        Mockito.verify(resultSet, times(1)).getString("categoria_nome");
        Mockito.verify(resultSet, times(1)).getInt("copie_vendute");
    }

    @Test
    @DisplayName("TCU4_5_2 doRetrieveByIsbnTest- Prodotto Non Esistente")
    public void doRetrieveByIsbnTest_NotFound() throws Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Mockito.when(resultSet.next()).thenReturn(false);

        Prodotto prodotto = prodottoIDS.doRetrieveByIsbn("10000000000000016");

        assertNull(prodotto);


        Mockito.verify(preparedStatement, times(1)).setString(1,"10000000000000016");
        Mockito.verify(resultSet, times(1)).next();
        Mockito.verify(preparedStatement, times(1)).executeQuery();
    }


    @Test
    @DisplayName("TCU4_6_1 doRetrieveByNomeTest- Prodotto Esistente")
    public void doRetrieveByNomeTest() throws Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getString("isbn")).thenReturn("10000000000000016");
        Mockito.when(resultSet.getString("autore")).thenReturn("Eiichiro Oda");
        Mockito.when(resultSet.getString("descrizione")).thenReturn("Rufy e i Cappelli di Paglia si dirigono verso il Grand Line, una pericolosa zona piena di avventure e segreti. Durante il loro viaggio, si imbattono in nuovi alleati e nemici.");
        Mockito.when(resultSet.getString("immagine_prod")).thenReturn("./images/op2.jpg");
        Mockito.when(resultSet.getDouble("prezzo")).thenReturn(5.45);
        Mockito.when(resultSet.getInt("quantita")).thenReturn(45);
        Mockito.when(resultSet.getString("genere_nome")).thenReturn("Avventura");
        Mockito.when(resultSet.getString("categoria_nome")).thenReturn("Shonen");
        Mockito.when(resultSet.getInt("copie_vendute")).thenReturn(10);

        Prodotto prodotto = prodottoIDS.doRetrieveByNome("One Piece 2");

        assertNotNull(prodotto);

        assertEquals("One Piece 2", prodotto.getNome());
        assertEquals("10000000000000016", prodotto.getIsbn());
        assertEquals("Eiichiro Oda", prodotto.getAutore());
        assertEquals("Rufy e i Cappelli di Paglia si dirigono verso il Grand Line, una pericolosa zona piena di avventure e segreti. Durante il loro viaggio, si imbattono in nuovi alleati e nemici.", prodotto.getDescrizione());
        assertEquals("./images/op2.jpg" , prodotto.getImmagine());
        assertEquals(Double.valueOf(5.45), prodotto.getPrezzo());
        assertEquals(Integer.valueOf(45) , prodotto.getQuantita());
        assertEquals("Avventura" , prodotto.getGenere());
        assertEquals("Shonen", prodotto.getCategoria());
        assertEquals(Integer.valueOf(10), prodotto.getCopieVendute());

        Mockito.verify(preparedStatement, times(1)).setString(1,"One Piece 2");
        Mockito.verify(resultSet, times(1)).next();
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(1)).getString("isbn");
        Mockito.verify(resultSet, times(1)).getString("autore");
        Mockito.verify(resultSet, times(1)).getString("descrizione");
        Mockito.verify(resultSet, times(1)).getString("immagine_prod");
        Mockito.verify(resultSet, times(1)).getDouble("prezzo");
        Mockito.verify(resultSet, times(1)).getInt("quantita");
        Mockito.verify(resultSet, times(1)).getString("genere_nome");
        Mockito.verify(resultSet, times(1)).getString("categoria_nome");
        Mockito.verify(resultSet, times(1)).getInt("copie_vendute");
    }

    @Test
    @DisplayName("TCU4_6_2 doRetrieveByNomeTest_NotFound- Prodotto Non Esistente")
    public void doRetrieveByNomeTest_NotFound() throws Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Mockito.when(resultSet.next()).thenReturn(false);

        Prodotto prodotto = prodottoIDS.doRetrieveByNome("One Piece 2");

        assertNull(prodotto);

        Mockito.verify(preparedStatement, times(1)).setString(1,"One Piece 2");
        Mockito.verify(resultSet, times(1)).next();
        Mockito.verify(preparedStatement, times(1)).executeQuery();
    }


    @Test
    @DisplayName("TCU4_7_1 lastSavedTest")
    public void lastSaved() throws Exception {
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Mockito.when(resultSet.next()).thenReturn(true,true,false);
        Mockito.when(resultSet.getString("isbn")).thenReturn("10000000000000016", "10000000000000010");
        Mockito.when(resultSet.getString("nome")).thenReturn("One Piece 2", "Dragon Ball Completo");
        Mockito.when(resultSet.getString("autore")).thenReturn("Eiichiro Oda", "Akira Toriyama");
        Mockito.when(resultSet.getString("descrizione")).thenReturn("Rufy e i Cappelli di Paglia si dirigono verso il Grand Line, una pericolosa zona piena di avventure e segreti. Durante il loro viaggio, si imbattono in nuovi alleati e nemici.","La storia inizia con il giovane Goku, un ragazzo dotato di forza sovrumana e una coda di scimmia. Goku incontra Bulma, una brillante scienziata, e insieme intraprendono la ricerca delle sette sfere del drago, oggetti magici che, quando riuniti, permettono di evocare il Drago Shenron, in grado di esaudire un desiderio.");
        Mockito.when(resultSet.getString("immagine_prod")).thenReturn("./images/op2.jpg", "./images/dbCompleto.jpg");
        Mockito.when(resultSet.getDouble("prezzo")).thenReturn(5.45,135.99);
        Mockito.when(resultSet.getInt("quantita")).thenReturn(45 ,3);
        Mockito.when(resultSet.getString("genere_nome")).thenReturn("Avventura", "Combattimento");
        Mockito.when(resultSet.getString("categoria_nome")).thenReturn("Shonen", "Shonen");
        Mockito.when(resultSet.getInt("copie_vendute")).thenReturn(10, 20);

        Collection<Prodotto> result = prodottoIDS.lastSaved();

        assertEquals(2, result.size());

        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(3)).next();
        Mockito.verify(resultSet, times(2)).getString("isbn");
        Mockito.verify(resultSet, times(2)).getString("nome");
        Mockito.verify(resultSet, times(2)).getString("autore");
        Mockito.verify(resultSet, times(2)).getString("descrizione");
        Mockito.verify(resultSet, times(2)).getString("immagine_prod");
        Mockito.verify(resultSet, times(2)).getDouble("prezzo");
        Mockito.verify(resultSet, times(2)).getInt("quantita");
        Mockito.verify(resultSet, times(2)).getString("genere_nome");
        Mockito.verify(resultSet, times(2)).getString("categoria_nome");
        Mockito.verify(resultSet, times(2)).getInt("copie_vendute");

        resultSet.close();
    }
    
    @Test
    @DisplayName("TCU4_8_1 doRetrieveAllProductsNameTest")
    public void doRetrieveAllProductsNameTest() throws  Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);


        Mockito.when(resultSet.next()).thenReturn(true,true,true ,false);
        Mockito.when(resultSet.getString("nome")).thenReturn("One piece 2", "Dragonball 1", "Berserk 5");

        Collection<String> result = prodottoIDS.doRetrieveAllProductsName();
        assertEquals(3, result.size());

        Mockito.verify(preparedStatement,times(1)).executeQuery();
        Mockito.verify(resultSet,times(4)).next();
    }


    @Test
    @DisplayName("TCU4_9_1 updateCopieVenduteTest- Prodotto Aggiornato")
    public void updateCopieVenduteTest() throws Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Prodotto prodotto = new Prodotto("98247387402384203", "Demon Slayer", "Antonio Taranto(Totò)", "L'avvincente storia di una bionda gigantesca che conquisterà il cuore del nostro protagonista", "./images/DemonSlayer", 104.00,104, "Psicologico" , "Manga Italiani", 1);

         assertTrue(prodottoIDS.updateCopieVendute(prodotto));

        Mockito.verify(preparedStatement,times(1)).setInt(1, prodotto.getCopieVendute());
        Mockito.verify(preparedStatement,times(1)).setString(2, prodotto.getIsbn());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    @DisplayName("TCU4_9_2 updateCopieVenduteTest-Prodotto Non Aggiornato")
    public void updateCopieVenduteTest_NotFound() throws Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(preparedStatement.executeUpdate()).thenReturn(0);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Prodotto prodotto = new Prodotto("98247387402384203", "Demon Slayer", "Antonio Taranto(Totò)", "L'avvincente storia di una bionda gigantesca che conquisterà il cuore del nostro protagonista", "./images/DemonSlayer", 104.00,104, "Psicologico" , "Manga Italiani", 1);

        assertFalse(prodottoIDS.updateCopieVendute(prodotto));



        Mockito.verify(preparedStatement,times(1)).setInt(1, prodotto.getCopieVendute());
        Mockito.verify(preparedStatement,times(1)).setString(2, prodotto.getIsbn());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
    }


    @Test
    @DisplayName("TCU4_10_1 bestSellerTest")
    public void bestSellerTest() throws Exception {
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Mockito.when(resultSet.next()).thenReturn(true,true,false);
        Mockito.when(resultSet.getString("isbn")).thenReturn("10000000000000016", "10000000000000010");
        Mockito.when(resultSet.getString("nome")).thenReturn("One Piece 2", "Dragon Ball Completo");
        Mockito.when(resultSet.getString("autore")).thenReturn("Eiichiro Oda", "Akira Toriyama");
        Mockito.when(resultSet.getString("descrizione")).thenReturn("Rufy e i Cappelli di Paglia si dirigono verso il Grand Line, una pericolosa zona piena di avventure e segreti. Durante il loro viaggio, si imbattono in nuovi alleati e nemici.","La storia inizia con il giovane Goku, un ragazzo dotato di forza sovrumana e una coda di scimmia. Goku incontra Bulma, una brillante scienziata, e insieme intraprendono la ricerca delle sette sfere del drago, oggetti magici che, quando riuniti, permettono di evocare il Drago Shenron, in grado di esaudire un desiderio.");
        Mockito.when(resultSet.getString("immagine_prod")).thenReturn("./images/op2.jpg", "./images/dbCompleto.jpg");
        Mockito.when(resultSet.getDouble("prezzo")).thenReturn(5.45,135.99);
        Mockito.when(resultSet.getInt("quantita")).thenReturn(45 ,3);
        Mockito.when(resultSet.getString("genere_nome")).thenReturn("Avventura", "Combattimento");
        Mockito.when(resultSet.getString("categoria_nome")).thenReturn("Shonen", "Shonen");
        Mockito.when(resultSet.getInt("copie_vendute")).thenReturn(10, 20);

        Collection<Prodotto> result = prodottoIDS.bestSellers();

        assertEquals(2, result.size());

        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(3)).next();
        Mockito.verify(resultSet, times(2)).getString("isbn");
        Mockito.verify(resultSet, times(2)).getString("nome");
        Mockito.verify(resultSet, times(2)).getString("autore");
        Mockito.verify(resultSet, times(2)).getString("descrizione");
        Mockito.verify(resultSet, times(2)).getString("immagine_prod");
        Mockito.verify(resultSet, times(2)).getDouble("prezzo");
        Mockito.verify(resultSet, times(2)).getInt("quantita");
        Mockito.verify(resultSet, times(2)).getString("genere_nome");
        Mockito.verify(resultSet, times(2)).getString("categoria_nome");
        Mockito.verify(resultSet, times(2)).getInt("copie_vendute");
    }

}
