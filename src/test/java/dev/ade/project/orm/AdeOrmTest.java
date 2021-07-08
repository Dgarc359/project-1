package dev.ade.project.orm;

import dev.ade.project.exception.ArgumentFormatException;
import dev.ade.project.pojo.PostPojo;
import dev.ade.project.util.ConnectionUtil;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
        Field postField8 = new Field("timestamp", LocalDateTime.now());

        List<Field> postFields = Arrays.asList
                (postField2,postField3,postField4,postField5,postField6,postField7,postField8);

        assertTrue(adeOrm.add("post",postFields, -1));
    }

    @Test
    public void addSinglePostWithNonDefaultIdCriteriaTest() throws ArgumentFormatException{
        Field postField1 = new Field("post_id", 10);
        Field postField2 = new Field("user_id",1);
        Field postField3 = new Field("title","Shrimp Linguini Alfredo");
        Field postField4 = new Field("country","United States");
        Field postField5 = new Field("city","Miami");
        Field postField6 = new Field("tag","food");
        Field postField7 = new Field("rating",5);

        List<Field> postFields = Arrays.asList
                (postField1, postField2,postField3,postField4,postField5,postField6,postField7);

        assertTrue(adeOrm.add("post",postFields));
    }

    @Test
    public void addObjectPostTest() throws ArgumentFormatException{
        PostPojo postPojoJo = new PostPojo
                (11,1,"Shrimp Linguini Alfredo","United States","Miami","food",4);

        assertTrue(adeOrm.add(postPojoJo));
    }

    @Test
    public void addSinglePostWithNonIntegerPrimaryKeyTest() throws ArgumentFormatException{

        Field field1 = new Field("prim_key","test");
        List<Field> fields = Arrays.asList(field1);

        assertTrue(adeOrm.add("test_table",fields));
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

    @Test
    public  void testUpdateSingleAttribute() throws ArgumentFormatException {
        Field field1 = new Field("title", "Neapolitan Ice Cream");
        Field field2 = new Field("city", "Ft. Collins");
        Field pk = new Field("post_id", 3);
        List<Field> fields = Arrays.asList(field1, field2);
        assertTrue(adeOrm.update("post", fields, pk));
    }

    @Test
    public  void testUpdateMultipleAttributes() throws ArgumentFormatException {
        Field field = new Field("city", "Ft. Collins");
        Field pk = new Field("post_id", 3);
        List<Field> fields = Arrays.asList(field);
        assertTrue(adeOrm.update("post", fields, pk));
    }

    @AfterAll
    public static void runTeardown() throws SQLException, FileNotFoundException {
        try (Connection connection = ConnectionUtil.getConnection()) {
            RunScript.execute(connection, new FileReader("teardown.sql"));
        }

    }

}

