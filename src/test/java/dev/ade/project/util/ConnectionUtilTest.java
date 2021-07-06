package dev.ade.project.util;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ConnectionUtilTest {

    @Test
    public void testConnection() throws SQLException {
        assertEquals("H2 JDBC Driver", ConnectionUtil.getConnection().getMetaData().getDriverName());
    }
}
