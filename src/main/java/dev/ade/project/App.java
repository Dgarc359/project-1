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

        } catch (ArgumentFormatException e) {
            e.printStackTrace();
        }
    }
}
