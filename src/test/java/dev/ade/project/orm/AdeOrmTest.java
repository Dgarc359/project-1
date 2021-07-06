package dev.ade.project.orm;

import dev.ade.project.exception.ArgumentFormatException;
import dev.ade.project.pojo.PostPojo;
import dev.ade.project.util.ConnectionUtil;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AdeOrmTest {
    private AdeOrm adeOrm = new AdeOrm(ConnectionUtil.getConnection());

    PostPojo pj = new PostPojo(1,"Shrimp Linguini Alfredo", "United States", "Miami", "food", 3);


    @BeforeAll
    public static void runSetup() throws SQLException, FileNotFoundException {
        try (Connection connection = ConnectionUtil.getConnection()) {
            RunScript.execute(connection, new FileReader("setup.sql"));
        }
    }

    /*@Test
    public void getTestInt() throws ArgumentFormatException {
        assertEquals("first", adeOrm.get("tableA", "stringCol", "id", 1));
    }


    @Test
    public void getTestNull() throws ArgumentFormatException {
        assertNull(adeOrm.get("tableA", "stringCol", "id", null));
    }

    @Test
    public void getTestObject() throws ArgumentFormatException {
        List<String> columnNames = Arrays.asList("id", "stringCol", "numericCol", "dateCol");
        assertEquals(2, adeOrm.get("tableA", columnNames, BigDecimal.valueOf(10), "numericCol").size());
    }

    @Test
    public void getTestGetAllRecords() throws ArgumentFormatException {
        List<String> columnNames = Arrays.asList("id", "stringCol", "booleanCol", "numericCol", "dateCol");
        assertEquals(3, adeOrm.get("tableA", columnNames).size());
    }

    @Test
    public void getTestAndConditions() throws ArgumentFormatException {
        List<String> columnNames = Arrays.asList("id", "stringCol", "numericCol", "dateCol");
        Field condition1 = new Field("numericCol", BigDecimal.valueOf(10));
        Field condition2 = new Field("booleanCol", 1);
        List<Field> conditions = Arrays.asList(condition1, condition2);
        assertEquals(1, adeOrm.get("tableA", columnNames, conditions, "and").size());
    }

    @Test
    public void testGetRecordsWithOrFields() throws ArgumentFormatException {
        List<String> columnNames = Arrays.asList("id", "stringCol", "numericCol", "dateCol");
        Field condition1 = new Field("numericCol", BigDecimal.valueOf(10));
        Field condition2 = new Field("booleanCol", 1);
        List<Field> conditions = Arrays.asList(condition1, condition2);
        assertEquals(3, adeOrm.get("tableA", columnNames, conditions, "or").size());
    }*/

    @Test
    public void addSingleUserTest() throws ArgumentFormatException{


        assertTrue(adeOrm.add("users", "delta", "password123"));
    }

    /*@Test
    public void addUserPasswordList() throws ArgumentFormatException{
        List<String> usernamePassword1 = Arrays.asList("echo","password123");
        List<String> usernamePassword2 = Arrays.asList("foxtrot","password123");
        List<String> usernamePassword3 = Arrays.asList("golf","password123");

        List<List<String>> usernamePasswordList = new ArrayList<List<String>>();
        usernamePasswordList.add(usernamePassword1);
        usernamePasswordList.add(usernamePassword2);
        usernamePasswordList.add(usernamePassword3);


        assertTrue(adeOrm.add("users", usernamePasswordList));
    }*/

    @Test
    public void addSinglePostTest() throws ArgumentFormatException{
        assertTrue(adeOrm.add(pj.getTableName(),pj.getUser_id(),pj.getTitle(),pj.getCountry(),pj.getCity(),pj.getTableName(),pj.getRating()));
    }

    @AfterAll
    public static void runTeardown() throws SQLException, FileNotFoundException {
        try (Connection connection = ConnectionUtil.getConnection()) {
            RunScript.execute(connection, new FileReader("teardown.sql"));
        }

    }

}

