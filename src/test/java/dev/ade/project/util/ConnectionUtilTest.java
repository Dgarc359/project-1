package dev.ade.project.util;

import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class ConnectionUtilTest {

    @Test
    public void testConnection() throws SQLException {
        assertEquals("PostgreSQL JDBC Driver", ConnectionUtil.getConnection().getMetaData().getDriverName());
    }
}
