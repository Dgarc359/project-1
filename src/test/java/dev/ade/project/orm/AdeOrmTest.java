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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AdeOrmTest {
    private AdeOrm adeOrm = new AdeOrm(ConnectionUtil.getConnection());

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

    /*@Test
    public void addSingleUserTest() throws ArgumentFormatException{
        Field field1 = new Field("username", "foxtrot");
        Field field2 = new Field("user_password", "Password123");

        List<Field> fields = Arrays.asList(field1,field2);

        assertTrue(adeOrm.add("users",fields,"default"));
    }*/

    @Test
    public void addSinglePostWithDefaultIdCriteriaTest() throws ArgumentFormatException{

        Field postField2 = new Field("user_id",1);
        Field postField3 = new Field("title","Shrimp Linguini Alfredo");
        Field postField4 = new Field("country","United States");
        Field postField5 = new Field("city","Miami");
        Field postField6 = new Field("tag","food");
        Field postField7 = new Field("rating",5);

        List<Field> postFields = Arrays.asList
                (postField2,postField3,postField4,postField5,postField6,postField7);

        assertTrue(adeOrm.add("post",postFields,-1));
    }

    @Test
    public void addSinglePostWithNonDefaultIdCriteriaTest() throws ArgumentFormatException{

        Field postField2 = new Field("user_id",1);
        Field postField3 = new Field("title","Shrimp Linguini Alfredo");
        Field postField4 = new Field("country","United States");
        Field postField5 = new Field("city","Miami");
        Field postField6 = new Field("tag","food");
        Field postField7 = new Field("rating",5);

        List<Field> postFields = Arrays.asList
                (postField2,postField3,postField4,postField5,postField6,postField7);

        assertTrue(adeOrm.add("post",postFields,10));
    }

    @Test
    public void addSinglePostWithNonIntegerPrimaryKeyTest() throws ArgumentFormatException{

        Field field1 = new Field("prim_key","test");
        List<Field> fields = Arrays.asList(field1);

        assertTrue(adeOrm.add("test_table",fields));
    }

    @AfterAll
    public static void runTeardown() throws SQLException, FileNotFoundException {
        try (Connection connection = ConnectionUtil.getConnection()) {
            RunScript.execute(connection, new FileReader("teardown.sql"));
        }

    }

}

