package dev.ade.project.orm;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AdeOrmTest {
    private AdeOrm adeOrm = new AdeOrm();

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
}
