package dev.ade.project.orm;

import dev.ade.project.exception.ArgumentFormatException;
import dev.ade.project.pojo.Post;
import dev.ade.project.pojo.User;
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
    private AdeOrm adeOrm = new AdeOrm();

    User user = new User();
    Class<?> userClass = user.getClass();
    AdeOrm uAdeOrm = new AdeOrm(userClass);

    Post post = new Post();
    Class<?> postClass = post.getClass();
    AdeOrm pAdeOrm = new AdeOrm(postClass);

    @BeforeAll
    public static void runSetup() throws SQLException, FileNotFoundException {
        try (Connection connection = ConnectionUtil.getConnection()) {
            RunScript.execute(connection, new FileReader("setup.sql"));
        }
    }

    @Test
    public void getTestPrimaryKey() throws ArgumentFormatException {
        User user = (User) uAdeOrm.get( "user_id", 1);
        assertEquals(1, user.getUserId());
    }

    @Test
    public void getTestNull() throws ArgumentFormatException {
        assertNull(uAdeOrm.get( "user_id",  null));
    }

    @Test
    public void getTestNotPrimaryKey() throws ArgumentFormatException {
        assertNull(uAdeOrm.get( "first",  null));
    }


    @Test
    public void getColumnsTest() throws ArgumentFormatException {
        assertEquals("alpha", uAdeOrm.getColumns("user_id", 1, "username").get(0));
    }

    @Test
    public void getRecordsInOrderTestSort() throws ArgumentFormatException {
        List<String> columnNames = Arrays.asList("city");
        assertEquals("New Orleans", pAdeOrm.getRecordsInOrder(columnNames, "country",
                "United States", "rating","asc").get(0).get(0));
    }

    @Test
    public void getAllTestNumber() throws ArgumentFormatException {
        assertEquals(3, uAdeOrm.getAll().size());
    }

    @Test
    public void getAllInOrderTestOrder() throws ArgumentFormatException {
        User user = (User) uAdeOrm.getAllInOrder("user_id", "desc").get(0);
        assertEquals("charlie",user.getUsername());
    }

    @Test
    public void getWithCriterionTest() throws ArgumentFormatException {
        List<String> columnNames = Arrays.asList("user_id", "title", "country", "city", "rating");
        FieldPair condition1 = new FieldPair("user_id", 2);
        FieldPair condition2 = new FieldPair("rating", 5);
        List<FieldPair> conditions = Arrays.asList(condition1, condition2);
        assertEquals(1, pAdeOrm.getWithCriterion(columnNames, conditions, "and").size());
    }

    @Test
    public void getRecordsWithOrTest() throws ArgumentFormatException {
        List<String> columnNames = Arrays.asList("user_id", "title", "country", "city", "rating");
        FieldPair condition1 = new FieldPair("user_id", 1);
        FieldPair condition2 = new FieldPair("rating", 5);
        List<FieldPair> conditions = Arrays.asList(condition1, condition2);
        assertEquals(5, pAdeOrm.getWithCriterion(columnNames, conditions, "or").size());
    }

    @Test
    public void getJointTest() throws ArgumentFormatException {
        List<String> columnNames = Arrays.asList("country", "city", "rating");
        assertEquals(5,  uAdeOrm.getJoint("inner", "users.user_id", "post", "post.user_id",
                columnNames).size());
    }

    @Test
    public void getJointWhereTest() throws ArgumentFormatException {
        List<String> columnNames = Arrays.asList("country", "city", "rating");
        assertEquals(2,  uAdeOrm.getJointWhere("inner", "users.user_id", "post", "post.user_id",
                columnNames, "tag", "food").size());
    }

    /*@Test
    public void addSingleUserTest() throws ArgumentFormatException{
        Field field1 = new Field("username", "foxtrot");
        Field field2 = new Field("user_password", "Password123");

        List<Field> fields = Arrays.asList(field1,field2);

        assertTrue(adeOrm.add("users",fields,"default"));
    }

    @Test
    public void addSinglePostWithDefaultIdCriteriaTest() throws ArgumentFormatException{

        FieldPair postFieldPair2 = new FieldPair("user_id",1);
        FieldPair postFieldPair3 = new FieldPair("title","Shrimp Linguini Alfredo");
        FieldPair postFieldPair4 = new FieldPair("country","United States");
        FieldPair postFieldPair5 = new FieldPair("city","Miami");
        FieldPair postFieldPair6 = new FieldPair("tag","food");
        FieldPair postFieldPair7 = new FieldPair("rating",5);
        FieldPair postFieldPair8 = new FieldPair("timestamp", LocalDateTime.now());

        List<FieldPair> postFieldPairs = Arrays.asList
                (postFieldPair2, postFieldPair3, postFieldPair4, postFieldPair5, postFieldPair6, postFieldPair7, postFieldPair8);

        assertTrue(adeOrm.add("post", postFieldPairs, -1));
    }*/

    /*@Test
    public void addSinglePostWithNonDefaultIdCriteriaTest() throws ArgumentFormatException{
        FieldPair postFieldPair1 = new FieldPair("post_id", 10);
        FieldPair postFieldPair2 = new FieldPair("user_id",1);
        FieldPair postFieldPair3 = new FieldPair("title","Shrimp Linguini Alfredo");
        FieldPair postFieldPair4 = new FieldPair("country","United States");
        FieldPair postFieldPair5 = new FieldPair("city","Miami");
        FieldPair postFieldPair6 = new FieldPair("tag","food");
        FieldPair postFieldPair7 = new FieldPair("rating",5);

        List<FieldPair> postFieldPairs = Arrays.asList
                (postFieldPair1, postFieldPair2, postFieldPair3, postFieldPair4, postFieldPair5, postFieldPair6, postFieldPair7);

        assertTrue(adeOrm.add("post", postFieldPairs));
    }*/

    @Test
    public void addObjectPostTest() throws ArgumentFormatException{
        /*Post postJo = new Post
                (11,1,"Shrimp Linguini Alfredo","United States","Miami","food",4);
*/

        User user = new User
                (0,"Tyler","Kelly",'M',"hardstuckwarrior","password123");
        AdeOrm userAdeOrm = new AdeOrm(User.class);

        assertTrue(userAdeOrm.add(user));
    }

    @Test
    public void addSinglePostWithNonIntegerPrimaryKeyTest() throws ArgumentFormatException{

        FieldPair fieldPair1 = new FieldPair("prim_key","test");
        List<FieldPair> fieldPairs = Arrays.asList(fieldPair1);

        assertTrue(adeOrm.add("test_table", fieldPairs));
    }

    @Test
    public  void testUpdateSingleAttribute() throws ArgumentFormatException {
        FieldPair field1 = new FieldPair("title", "Neapolitan Ice Cream");
        FieldPair field2 = new FieldPair("city", "Ft. Collins");
        FieldPair pk = new FieldPair("post_id", 3);
        List<FieldPair> fields = Arrays.asList(field1, field2);
        assertTrue(adeOrm.update("post", fields, pk));
    }

    @Test
    public  void testUpdateMultipleAttributes() throws ArgumentFormatException {
        FieldPair field = new FieldPair("city", "Ft. Collins");
        FieldPair pk = new FieldPair("post_id", 3);
        List<FieldPair> fields = Arrays.asList(field);
        assertTrue(adeOrm.update("post", fields, pk));
    }

    public void testDeleteARecord() throws ArgumentFormatException {
        assertTrue(adeOrm.delete("post", "post_id", 3));
    }

    @AfterAll
    public static void runTeardown() throws SQLException, FileNotFoundException {
        try (Connection connection = ConnectionUtil.getConnection()) {
            RunScript.execute(connection, new FileReader("teardown.sql"));
        }

    }

}

