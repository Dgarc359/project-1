package dev.ade.project;

import dev.ade.project.exception.ArgumentFormatException;
import dev.ade.project.orm.AdeOrm;
import dev.ade.project.orm.Field;
import dev.ade.project.util.ConnectionUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

// This class only serves for checking results during development, will be commented out or deleted at deployment.
public class App {

    public static void main (String[] args) {
        // For checking results
        try {
            AdeOrm adeOrm = new AdeOrm(ConnectionUtil.getConnection());

            String result0 = adeOrm.get("users","username","user_id",1);
            System.out.println(result0);
            Field field1 = new Field("username", "foxtrot");
            Field field2 = new Field("user_password", "Password123");

            List<Field> fields = Arrays.asList(field1,field2);
            //System.out.println(adeOrm.add("users",fields,"default"));




        } catch (ArgumentFormatException e) {
            e.printStackTrace();
        }
    }
}
