package dev.ade.project;

import dev.ade.project.exception.ArgumentFormatException;
import dev.ade.project.orm.AdeOrm;
import dev.ade.project.orm.Field;
import dev.ade.project.util.ConnectionUtil;

import java.math.BigDecimal;
import java.util.*;

// This class only serves for checking results during development, will be commented out or deleted at deployment.
public class App {

    public static void main (String[] args) {
        // For checking results
        try {
            AdeOrm adeOrm = new AdeOrm(ConnectionUtil.getConnection());

            String result0 = adeOrm.get("users","username","user_id",1);
            System.out.println(result0);

            List<String> columnNames = Arrays.asList("post_id", "user_id", "country", "city", "rating");
            List<Object> result = adeOrm.get("post", columnNames, "rating", 5);
            System.out.println(result);

            List<String> columnNames2 = Arrays.asList("user_id", "username", "user_password");
            List<List<Object>> result2 = adeOrm.get("users", columnNames2);
            System.out.println(result2);

            List<String> columnList = Arrays.asList("title", "city");
            List<String> valuesList = Arrays.asList("Chocolate Ice Cream", "Denver");
            Field fielda = new Field("title", "Neapolitan Ice Cream");
            Field fieldb = new Field("city", "Ft. Collins");
            Field pk = new Field("post_id", 3);

            List<Field> fieldss = Arrays.asList(fielda,fieldb);

//            Object[] valuesList = {"Chocolate Ice Cream"};

            Field field1 = new Field("rating", 5);
            Field field2 = new Field("city", "Miami");
            List<Field> fields = Arrays.asList(field1, field2);
            List<List<Object>> result3 = adeOrm.get("post", columnNames, fields, "and");
            result3.forEach(System.out::println);

            boolean result1 = adeOrm.update("post", fields, pk);
            System.out.println(result1);

        } catch (ArgumentFormatException e) {
            e.printStackTrace();
        }
    }
}
