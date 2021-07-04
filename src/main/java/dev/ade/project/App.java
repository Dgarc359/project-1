package dev.ade.project;

import dev.ade.project.orm.AdeOrm;
import dev.ade.project.orm.Condition;
import dev.ade.project.util.ConnectionUtil;

import java.math.BigDecimal;
import java.util.*;

// This class only serves for checking results during development, will be commented out or deleted at deployment.
public class App {

    public static void main (String[] args) {
        // For checking results
        AdeOrm adeOrm = new AdeOrm(ConnectionUtil.getConnection());
        String result0 = adeOrm.getStringColumn("bank_user", "first_name", "userid", "harry");
        System.out.println(result0);

        String result1 = adeOrm.getColumn("transfer_transaction", "datetime", "amount", BigDecimal.valueOf(100));
        System.out.println(result1);

        List<String> columnNames = Arrays.asList("userid", "first_name", "last_name", "pin", "status", "last_login");
        List<Object> result2 = adeOrm.getRecord("bank_user", columnNames, "userid", "harry");
        System.out.println(result2);

        List<String> columnNames2 = Arrays.asList("trans_type", "userid", "datetime", "amount");
        List<List<Object>> result3 = adeOrm.getRecords("transfer_transaction", columnNames2, "amount", BigDecimal.valueOf(10));
        System.out.println(result3);

        List<List<Object>> result4 = adeOrm.getRecords("transfer_transaction", columnNames2);
        System.out.println(result4);

        List<String> columnNames3 = Arrays.asList("trans_type", "ac_number", "amount", "datetime");
        Condition condition1 = new Condition("trans_type", "withdraw");
        Condition condition2 = new Condition("amount", BigDecimal.valueOf(500));
        Condition condition3 = new Condition("userid", "harry");
        List<Condition> conditions = Arrays.asList(condition1, condition2, condition3);
        List<List<Object>> result5 = adeOrm.getRecordsWithConditions("deposit_withdraw_transaction", columnNames3, conditions, "and");
        result5.forEach(System.out::println);

        Condition condition4 = new Condition("amount", BigDecimal.valueOf(1000));
        Condition condition5 = new Condition("amount", BigDecimal.valueOf(500));
        List<Condition> conditions2 = Arrays.asList(condition4, condition5);
        List<List<Object>> result6 = adeOrm.getRecordsWithConditions("deposit_withdraw_transaction", columnNames3, conditions2, "or");
        result6.forEach(System.out::println);
    }
}
