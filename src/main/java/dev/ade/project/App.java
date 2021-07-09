package dev.ade.project;

import dev.ade.project.exception.ArgumentFormatException;
import dev.ade.project.orm.AdeOrm;
import dev.ade.project.orm.FieldPair;

import java.sql.SQLException;
import java.util.*;

// This class only serves for checking results during development, will be commented out or deleted at deployment.
public class App {

    public static void main (String[] args) {

        // For checking results
        try {
            String url = "jdbc:postgresql://training-db.czu9b8kfiorj.us-east-2.rds.amazonaws.com:5432/postgres?currentSchema=project-1";
            final String USERNAME = System.getenv("USERNAME");
            final String PASSWORD = System.getenv("PASSWORD");
            AdeOrm.setConnection(url, USERNAME, PASSWORD);
            AdeOrm adeOrm = new AdeOrm();
            System.out.println(AdeOrm.getConn().getMetaData().getDriverName());
            String result0 = adeOrm.get("users","username","user_id",1);
            System.out.println(result0);

            List<String> columnNames = Arrays.asList("post_id", "user_id", "country", "city", "rating");
            List<Object> result = adeOrm.get("post", columnNames, "rating", 5);
            System.out.println(result);

            List<String> columnNames2 = Arrays.asList("user_id", "username", "user_password");
            List<List<Object>> result2 = adeOrm.get("users", columnNames2);
            System.out.println(result2);

            result2 = adeOrm.get("post", columnNames, 1, "user_id", "rating","desc");
            System.out.println(result2);

            List<String> columnList = Arrays.asList("title", "city");
            List<String> valuesList = Arrays.asList("Chocolate Ice Cream", "Denver");
            FieldPair fielda = new FieldPair("title", "Neapolitan Ice Cream");
            FieldPair fieldb = new FieldPair("city", "Ft. Collins");
            FieldPair pk = new FieldPair("post_id", 3);

            List<FieldPair> fieldsses = Arrays.asList(fielda,fieldb);

//            Object[] valuesList = {"Chocolate Ice Cream"};

            FieldPair fieldPair1 = new FieldPair("rating", 5);
            FieldPair fieldPair2 = new FieldPair("city", "Miami");
            List<FieldPair> fieldPairs = Arrays.asList(fieldPair1, fieldPair2);
            List<List<Object>> result3 = adeOrm.get("post", columnNames, fieldPairs, "and");
            result3.forEach(System.out::println);

            boolean result1 = adeOrm.update("post", fieldPairs, pk);
            System.out.println(result1);

        } catch (ArgumentFormatException  | SQLException e) {
            e.printStackTrace();
        }

    }
}
