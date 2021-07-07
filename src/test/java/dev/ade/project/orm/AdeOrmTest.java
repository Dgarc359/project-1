package dev.ade.project.orm;

import dev.ade.project.exception.ArgumentFormatException;
import dev.ade.project.util.ConnectionUtil;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AdeOrmTest {
    private AdeOrm adeOrm = new AdeOrm(ConnectionUtil.getConnection());

    @BeforeAll
    public static void runSetup() throws SQLException, FileNotFoundException {
        try (Connection connection = ConnectionUtil.getConnection()) {
            RunScript.execute(connection, new FileReader("setup.sql"));
        }
    }

    @Test
    public void getTestInt() throws ArgumentFormatException {
        assertEquals("alpha", adeOrm.get("users", "username", "user_id", 1));
    }


    @Test
    public void getTestNull() throws ArgumentFormatException {
        assertNull(adeOrm.get("users", "user_id", "id", null));
    }

    @Test
    public void getTestObject() throws ArgumentFormatException {
        List<String> columnNames = Arrays.asList("user_id", "username", "user_password");
        assertEquals(1, adeOrm.get("users", columnNames, (Object)"charlie", "username").size());
    }

    @Test
    public void getTestGetAllRecords() throws ArgumentFormatException {
        List<String> columnNames = Arrays.asList("user_id", "username", "user_password");
        assertEquals(3, adeOrm.get("users", columnNames).size());
    }

    @Test
    public void getTestAndConditions() throws ArgumentFormatException {
        List<String> columnNames = Arrays.asList("user_id", "title", "country", "city", "rating");
        Field condition1 = new Field("user_id", 1);
        Field condition2 = new Field("rating", 3);
        List<Field> conditions = Arrays.asList(condition1, condition2);
        assertEquals(1, adeOrm.get("post", columnNames, conditions, "and").size());
    }

    @Test
    public void testGetRecordsWithOrFields() throws ArgumentFormatException {
        List<String> columnNames = Arrays.asList("user_id", "title", "country", "city", "rating");
        Field condition1 = new Field("user_id", 1);
        Field condition2 = new Field("rating", 3);
        List<Field> conditions = Arrays.asList(condition1, condition2);
        assertEquals(3, adeOrm.get("post", columnNames, conditions, "or").size());
    }

    @Test
    public void testGetInnerJoinWithCondition() throws ArgumentFormatException {
        List<String> columnNames = Arrays.asList("username", "title", "country", "city", "rating");
        Field condition1 = new Field("user_id", 1);
        Field condition2 = new Field("rating", 3);
        List<Field> conditions = Arrays.asList(condition1, condition2);
        assertEquals(2, adeOrm.get("inner", "users", "users.user_id", "post", "post.user_id",
                columnNames, "users.user_id", 1).size());
    }

    @Test
    public void testGetLeftJoin() throws ArgumentFormatException {
        List<String> columnNames = Arrays.asList("username", "title", "country", "city", "rating");
        Field condition1 = new Field("user_id", 1);
        Field condition2 = new Field("rating", 3);
        List<Field> conditions = Arrays.asList(condition1, condition2);
        assertEquals(4, adeOrm.get("inner", "users", "users.user_id", "post", "post.user_id",
                columnNames).size());
    }

    @AfterAll
    public static void runTeardown() throws SQLException, FileNotFoundException {
        try (Connection connection = ConnectionUtil.getConnection()) {
            RunScript.execute(connection, new FileReader("teardown.sql"));
        }

    }

}

