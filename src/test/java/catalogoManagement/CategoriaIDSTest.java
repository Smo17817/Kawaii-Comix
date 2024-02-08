package catalogoManagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class CategoriaIDSTest {
    private DataSource ds;
    private Connection connection;
    private CategoriaIDS categoriaIDS;

    @BeforeEach
    public void setUp() throws Exception {

        ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection())
                .thenReturn(connection = mock(Connection.class));
        categoriaIDS = new CategoriaIDS(ds);
    }


    @Test
    @DisplayName("3_1_1 doRetrieveAll- Categorie Trovate")
    public void doRetrieveAll() throws Exception {
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);


        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Mockito.when(resultSet.next()).thenReturn(true,true,true,true , false);
        Mockito.when(resultSet.getString("nome")).thenReturn("Shonen", "Seinen", "Shojo", "Kodomo");


        Collection<String> result = categoriaIDS.doRetrieveAll();

        assertEquals(4, result.size());

        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(5)).next();
        Mockito.verify(resultSet, times(4)).getString("nome");
    }


    @Test
    @DisplayName("TCU3_2_1 checkCategoriaName- Categoria Presente")
    public void checkCategoriaName() throws Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);


        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);


        Mockito.when(resultSet.next()).thenReturn(true);

        assertTrue(categoriaIDS.checkCategoriaName("Manga Italiani"));

        Mockito.verify(preparedStatement,times(1)).setString(1, "Manga Italiani");
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet,times(1)).next();
    }

    @Test
    @DisplayName("TCU3_2_2 checkCategoriaName- Categoria Non Presente")
    public void checkCategoriaName_NotFound() throws Exception{
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);


        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);


        Mockito.when(resultSet.next()).thenReturn(false);

        assertFalse(categoriaIDS.checkCategoriaName("Hentai"));

        Mockito.verify(preparedStatement,times(1)).setString(1, "Hentai");
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet,times(1)).next();
    }
}
