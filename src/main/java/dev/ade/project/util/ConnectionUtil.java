package dev.ade.project.util;

import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The ConnectionUtil class provides a static getConnection method
 * to connect to the postrgresql database.
 */
public class ConnectionUtil {
    private static Connection connection;
    private static final boolean IS_TEST = Boolean.parseBoolean(System.getenv("TEST"));
    private static Logger logger = Log4j.getLogger();

    /**
     * The getConnection method returns a singleton Connection object.
     * A local database mirrors the actual deployed web database is used for testing.
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                if (IS_TEST) {
                    // connect to local host postgresql database for testing
                    connection = DriverManager.getConnection("jdbc:h2:~/test");
                } else {
                    String url = "jdbc:postgresql://training-db.czu9b8kfiorj.us-east-2.rds.amazonaws.com:5432/postgres?currentSchema=project-1";
                    final String PASSWORD = System.getenv("PASSWORD");
                    final String USERNAME = System.getenv("USERNAME");
                    //final String USERNAME = System.getenv("DB_USERNAME");
                    //final String PASSWORD = System.getenv("DB_PASSWORD");
                    connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        return connection;
    }
}

