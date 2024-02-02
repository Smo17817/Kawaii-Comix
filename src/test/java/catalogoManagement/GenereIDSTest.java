package catalogoManagement;

import acquistoManagement.GestoreOrdiniIDS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class GenereIDSTest {
    private DataSource ds;
    private Connection connection;
    private GenereIDS genereIDS;

    @BeforeEach
    public void setUp() throws Exception {

        ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection())
                .thenReturn(connection = mock(Connection.class));
        genereIDS = new GenereIDS(ds);
    }


    @Test
    @DisplayName("doRetrieveAll")
    public void doRetrieveAll() throws Exception {
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(1)).next();

    }
}
