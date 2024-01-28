package utenteManagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.mockito.Mockito;

import org.dbunit.Assertion;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.SortedTable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

public class UserIDSTest {

	private static String expectedPath = "db/expected/utenteManagement/";
    private static String initPath = "db/init/utenteManagement/";
    private static String table = "site_user";
    private static IDatabaseTester tester;
    private DataSource ds;
    private UserIDS userIDS;
    
    @BeforeAll
    static void setUpAll() throws ClassNotFoundException {
        // mem indica che il DB deve andare in memoria
        // test indica il nome del DB
        // DB_CLOSE_DELAY=-1 impone ad H2 di eliminare il DB solo quando il processo della JVM termina
        tester = new JdbcDatabaseTester(org.h2.Driver.class.getName(),
                "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;init=runscript from 'classpath:db/init_schema.sql'",
                "prova",
                ""
        );
        // Refresh permette di svuotare la cache dopo un modifica con setDataSet
        // DeleteAll ci svuota il DB manteneno lo schema
        tester.setSetUpOperation(DatabaseOperation.REFRESH);
        tester.setTearDownOperation(DatabaseOperation.DELETE_ALL);
    }
    
    private static void refreshDataSet(String filename) throws Exception {
        IDataSet initialState = new FlatXmlDataSetBuilder()
                .build(UserIDSTest.class.getClassLoader().getResourceAsStream(filename));
        tester.setDataSet(initialState);
        tester.onSetup();
    }
    
    @BeforeEach
    public void setUp() throws Exception {
        // Prepara lo stato iniziale di default
        refreshDataSet(initPath + "site_userInit.xml");
        ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection())
        .thenReturn(tester.getConnection().getConnection());
        userIDS = new UserIDS(ds);
    }

    @AfterEach
    public void tearDown() throws Exception {
        tester.onTearDown();
    }
    
    //casi di formato errato registrazione
    private static Stream<Arguments> doSaveTestProvider(){
    	return Stream.of(
    			//formato email non corretto
    			Arguments.of("mariorossi@gmail", "Prova123" ,"Mario", "Rossi", "Via Roma 1", "Salerno", "84100", "SA", "Italia"),
    			//email vuota
    			Arguments.of("",  "Prova123" ,"Mario", "Rossi", "Via Roma 1", "Salerno", "84100", "SA", "Italia"),
    			//email presente nel DB
    			Arguments.of("giovanni.sicilia02@gmail.com", "Prova123", "Giovanni", "Sicilia", "Via Nazario Sauro 3", "Salerno", "84135","SA","Italia"),
    			//nome vuoto
    			Arguments.of("mariorossi@gmail.com", "Prova123", "", "Rossi", "Via Roma 1", "Salerno","84100", "SA", "Italia"),
    			//cognome vuoto
    			Arguments.of("mariorossi@gmail.com", "Prova123", "Mario", "", "Via Roma 1", "Salerno","84100", "SA", "Italia"),
    			//indirizzo vuoto
    			Arguments.of("mariorossi@gmail.com", "Prova123", "Mario", "Rossi", "", "Salerno","84100", "SA", "Italia"),
    			//città vuota
    			Arguments.of("mariorossi@gmail.com", "Prova123", "Mario", "Rossi", "Via Roma 1", "", "84100", "SA", "Italia"),
    			//cap vuoto
    			Arguments.of("mariorossi@gmail.com", "Prova123", "Mario", "Rossi", "Via Roma 1", "Salerno", "", "SA", "Italia"),
    			//provincia vuota
    			Arguments.of("mariorossi@gmail.com", "Prova123", "Mario", "Rossi", "Via Roma 1", "Salerno", "84135", "", "Italia"),
    			//nazione vuota
    			Arguments.of("mariorossi@gmail.com", "Prova123", "Mario", "Rossi", "Via Roma 1", "Salerno", "84135", "SA", ""),
    			//formato password non corretto
    			Arguments.of("mariorossi@gmail.com", "Prova12" ,"Mario", "Rossi", "Via Roma 1", "Salerno","84100", "SA", "Italia")
    			);
    }
    
    @Test
    @DisplayName("TCU doSaveUserTestSalva")
    public void doSaveTestSalva() throws Exception {
    	ITable expectedTable = new FlatXmlDataSetBuilder()
                .build(UserIDSTest.class.getClassLoader().getResourceAsStream(expectedPath + "doSaveUser.xml"))
                .getTable(table);
    	
    	User user = new User("mariorossi@gmail.com","3f4d3f4b756ccdd3ced93161da88ec3e7bc9f41b79a29a5b1730ec2b618c9516", "Mario", "Rossi", "Via Roma 1","Salerno","84100","SA", "Italia");
    	
    	try {
			userIDS.doSaveUser(user);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	ITable actualTable = tester.getConnection().createDataSet().getTable(table);
        Assertion.assertEquals(new SortedTable(expectedTable), new SortedTable(actualTable));
    }
    
    @ParameterizedTest
    @MethodSource("doSaveUserTestProvider")
    @DisplayName("TCU doSaveTestNonSalva")
    public void doSaveTestNonSalva(String email,String password, String nome, String cognome, String indirizzo, String citta, String codice_postale, String provincia, String nazione) {
    	assertThrows(SQLException.class, () -> {
    		User user = new User(email, password,nome, cognome, indirizzo, citta, codice_postale, provincia,  nazione );
			userIDS.doSaveUser(user);
    	});
    }
}
