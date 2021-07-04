package dev.ade.project.orm;

import dev.ade.project.util.ConnectionUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AdeOrmTest {
    private AdeOrm adeOrm = new AdeOrm(ConnectionUtil.getConnection());

    @Test
    public void testGetStringColumn() {
        assertEquals("Harry", adeOrm.getStringColumn("bank_user", "first_name", "userid", "harry"));
    }

    @Test
    public void testGetOneColumn() {
        assertEquals("transfer", adeOrm.getColumn("transfer_transaction", "trans_type", "amount", BigDecimal.valueOf(100)));
    }

    @Test
    public void testGetRecord() {
        List<String> columnNames = Arrays.asList("userid", "first_name", "last_name", "pin", "status", "last_login");
        assertEquals("harry", adeOrm.getRecord("bank_user", columnNames, "userid", "harry").get(0));
    }

    @Test
    public void testGetRecords() {
        List<String> columnNames = Arrays.asList("trans_type", "datetime", "amount");
        assertEquals(2, adeOrm.getRecords("transfer_transaction", columnNames, "amount", BigDecimal.valueOf(10)).size());
    }

    @Test
    public void testGetAllRecords() {
        List<String> columnNames = Arrays.asList("trans_type", "datetime", "amount");
        assertEquals(8, adeOrm.getRecords("transfer_transaction", columnNames).size());
    }

    @Test
    public void testGetRecordsWithAndConditions() {
        List<String> columnNames = Arrays.asList("trans_type", "ac_number", "amount", "datetime");
        Condition condition1 = new Condition("trans_type", "withdraw");
        Condition condition2 = new Condition("amount", BigDecimal.valueOf(500));
        Condition condition3 = new Condition("userid", "harry");
        List<Condition> conditions = Arrays.asList(condition1, condition2, condition3);
        assertEquals(1, adeOrm.getRecordsWithConditions("deposit_withdraw_transaction", columnNames, conditions, "and").size());
    }

    @Test
    public void testGetRecordsWithOrConditions() {
        List<String> columnNames = Arrays.asList("trans_type", "ac_number", "amount", "datetime");
        Condition condition1 = new Condition("amount", BigDecimal.valueOf(1000));
        Condition condition2 = new Condition("amount", BigDecimal.valueOf(500));
        List<Condition> conditions = Arrays.asList(condition1, condition2);
        assertEquals(10, adeOrm.getRecordsWithConditions("deposit_withdraw_transaction", columnNames, conditions, "or").size());
    }
}
