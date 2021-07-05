package dev.ade.project.orm;

import dev.ade.project.exception.ArgumentFormatException;
import dev.ade.project.util.ConnectionUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdeOrmTest {
    private AdeOrm adeOrm = new AdeOrm(ConnectionUtil.getConnection());

    @Test
    public void testGetOneColumn() throws ArgumentFormatException {
        assertEquals("transfer", adeOrm.get("transfer_transaction", "trans_type", "amount", BigDecimal.valueOf(100)));
    }


    @Test
    public void testGetRecords() throws ArgumentFormatException {
        List<String> columnNames = Arrays.asList("trans_type", "datetime", "amount");
        assertEquals(2, adeOrm.get("transfer_transaction", columnNames, BigDecimal.valueOf(10), "amount").size());
    }

    @Test
    public void testGetAllRecords() throws ArgumentFormatException {
        List<String> columnNames = Arrays.asList("trans_type", "datetime", "amount");
        assertEquals(8, adeOrm.get("transfer_transaction", columnNames).size());
    }

    @Test
    public void testGetRecordsWithAndFields() throws ArgumentFormatException {
        List<String> columnNames = Arrays.asList("trans_type", "ac_number", "amount", "datetime");
        Field condition1 = new Field("trans_type", "withdraw");
        Field condition2 = new Field("amount", BigDecimal.valueOf(500));
        Field condition3 = new Field("userid", "harry");
        List<Field> conditions = Arrays.asList(condition1, condition2, condition3);
        assertEquals(1, adeOrm.get("deposit_withdraw_transaction", columnNames, conditions, "and").size());
    }

    @Test
    public void testGetRecordsWithOrFields() throws ArgumentFormatException {
        List<String> columnNames = Arrays.asList("trans_type", "ac_number", "amount", "datetime");
        Field condition1 = new Field("amount", BigDecimal.valueOf(1000));
        Field condition2 = new Field("amount", BigDecimal.valueOf(500));
        List<Field> conditions = Arrays.asList(condition1, condition2);
        assertEquals(10, adeOrm.get("deposit_withdraw_transaction", columnNames, conditions, "or").size());
    }
}

