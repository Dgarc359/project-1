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

    @BeforeAll
    public static void runSetup() throws SQLException, FileNotFoundException {
        try (Connection connection = ConnectionUtil.getConnection()) {
            RunScript.execute(connection, new FileReader("setup.sql"));
        }
    }

    /*@Test
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
    }

    @Test
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
    }

    @Test
    public void addObjectPostTest() throws ArgumentFormatException{
        Post postJo = new Post
                (11,1,"Shrimp Linguini Alfredo","United States","Miami","food",4);

        assertTrue(adeOrm.add(postJo));
    }

    @Test
    public void addSinglePostWithNonIntegerPrimaryKeyTest() throws ArgumentFormatException{

        FieldPair fieldPair1 = new FieldPair("prim_key","test");
        List<FieldPair> fieldPairs = Arrays.asList(fieldPair1);

        assertTrue(adeOrm.add("test_table", fieldPairs));
    }

    @Test
    public void testGetInnerJoinWithCondition() throws ArgumentFormatException {
        List<String> columnNames = Arrays.asList("username", "title", "country", "city", "rating");
        FieldPair condition1 = new FieldPair("user_id", 1);
        FieldPair condition2 = new FieldPair("rating", 3);
        List<FieldPair> conditions = Arrays.asList(condition1, condition2);
        assertEquals(2, adeOrm.get("inner", "users", "users.user_id", "post", "post.user_id",
                columnNames, "users.user_id", 1).size());
    }

    @Test
    public void testGetLeftJoin() throws ArgumentFormatException {
        List<String> columnNames = Arrays.asList("username", "title", "country", "city", "rating");
        FieldPair condition1 = new FieldPair("user_id", 1);
        FieldPair condition2 = new FieldPair("rating", 3);
        List<FieldPair> conditions = Arrays.asList(condition1, condition2);
        assertEquals(4, adeOrm.get("inner", "users", "users.user_id", "post", "post.user_id",
                columnNames).size());
    }

    public void testDeleteARecord() throws ArgumentFormatException {
        assertTrue(adeOrm.delete("post", "post_id", 3));
    }*/

    @Test
    public void testGet() throws ArgumentFormatException {
        /*List<Object> list = adeOrm.getAll();
        System.out.println(list);*/

        System.out.println
                (String.valueOf(adeOrm.get("users","username","user_id",1)));
    }

    @AfterAll
    public static void runTeardown() throws SQLException, FileNotFoundException {
        try (Connection connection = ConnectionUtil.getConnection()) {
            RunScript.execute(connection, new FileReader("teardown.sql"));
        }

    }



}

