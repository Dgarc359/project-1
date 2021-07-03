package dev.ade.project;

import dev.ade.project.orm.AdeOrm;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class App {

    public static void main (String[] args) {
        AdeOrm adeOrm = new AdeOrm();
        String result0 = adeOrm.getStringColumn("bank_user", "first_name", "userid", "harry");
        System.out.println(result0);
        String result1 = adeOrm.getColumn("transfer_transaction", "datetime", "amount", BigDecimal.valueOf(100));
        System.out.println(result1);
        List<String> columnNames = Arrays.asList("userid", "first_name", "last_name", "pin", "status", "last_login");
        List<Object> result2 = adeOrm.getRecord("bank_user", columnNames, "userid", "harry");
        System.out.println(result2);
        List<String> columnNames2 = Arrays.asList("trans_type", "datetime", "amount");
        List<List<Object>> result3 = adeOrm.getRecords("transfer_transaction", columnNames2, "amount", BigDecimal.valueOf(10));
        System.out.println(result3);
    }
}
