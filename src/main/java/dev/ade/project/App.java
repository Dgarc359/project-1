package dev.ade.project;

import dev.ade.project.exception.ArgumentFormatException;
import dev.ade.project.orm.AdeOrm;
import dev.ade.project.orm.FieldPair;
import dev.ade.project.pojo.PostPojo;
import dev.ade.project.util.ConnectionUtil;
import dev.ade.project.util.MapperUtil;

// This class only serves for checking results during development, will be commented out or deleted at deployment.
public class App {

    public static void main (String[] args) {
        // For checking results
        try {
            AdeOrm adeOrm = new AdeOrm(ConnectionUtil.getConnection());
//            PostPojo pp = new PostPojo(5, 3, "Strawberry Ice Cream", "Japan", "Akita", "tag", 5);
//
//            FieldPair fieldPair1 = new FieldPair("title", "Vanilla Ice Cream");
//
//            adeOrm.delete(pp);
////
//            System.out.println(MapperUtil.parseFields(pp));
//
//            // this gave a true value when column name didn't match.. hmm...
            boolean result1 = adeOrm.update("post","title","post_id",3, "Chocolate Ice Cream");
//            System.out.println(result1);

//            List<String> columnList = Arrays.asList("title", "city");
//            List<String> valuesList = Arrays.asList("Chocolate Ice Cream", "Denver");
//            Field field1 = new Field("title", "Vanilla Ice Cream");
//            Field field2 = new Field("city", "Ft. Collins");
//            Field pk = new Field("post_id", 3);
//
//            List<Field> fields = Arrays.asList(field1,field2);
//
//            Object[] valuesList = {"Chocolate Ice Cream"};
//
//
//            boolean result2 = adeOrm.delete("post","post_id",3);
//            System.out.println(result1);

            /*
            String result0 = adeOrm.getStringColumn("bank_user", "first_name", "userid", "harry");
            System.out.println(result0);
             */

//            String result1 = adeOrm.get("bank_user", "userid", "intcol", 10);
//            System.out.println(result1);
//
//            List<String> columnNames = Arrays.asList("userid", "first_name", "last_name", "pin", "status", "last_login");
//            List<Object> result2 = adeOrm.get("bank_user", columnNames, "userid", (Object)"harry");
//            System.out.println(result2);
//
//
//            List<String> columnNames2 = Arrays.asList("trans_type", "userid", "datetime", "amount");
//            List<List<Object>> result3 = adeOrm.get("transfer_transaction", columnNames2, BigDecimal.valueOf(10), "amount");
//            System.out.println(result3);
//
//            List<List<Object>> result4 = adeOrm.get("transfer_transaction", columnNames2);
//            System.out.println(result4);
//
//            List<String> columnNames3 = Arrays.asList("trans_type", "ac_number", "amount", "datetime");
//            Field field1 = new Field("trans_type", "withdraw");
//            Field field2 = new Field("amount", BigDecimal.valueOf(500));
//            Field field3 = new Field("userid", "harry");
//            List<Field> fields = Arrays.asList(field1, field2, field3);
//            List<List<Object>> result5 = adeOrm.get("deposit_withdraw_transaction", columnNames3, fields, "and");
//            result5.forEach(System.out::println);
//
//            Field field4 = new Field("amount", BigDecimal.valueOf(1000));
//            Field field5 = new Field("amount", BigDecimal.valueOf(500));
//            List<Field> fields2 = Arrays.asList(field4, field5);
//            List<List<Object>> result6 = adeOrm.get("deposit_withdraw_transaction", columnNames3, fields2, "or");
//            result6.forEach(System.out::println);
        } catch (ArgumentFormatException e) {
            e.printStackTrace();
        }
    }
}
