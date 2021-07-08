package dev.ade.project.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BasicConnectionPoolUtil implements ConnectionPool{

    /*private static final String url = "jdbc:postgresql://training-db.czu9b8kfiorj.us-east-2.rds.amazonaws.com:5432/postgres?currentSchema=project-1";
    private static final String password = System.getenv("PASSWORD");
    private static final String username = System.getenv("USERNAME");*/
    private static final String url = "jdbc:h2:mem:test";
    private static final String password = System.getenv("PASSWORD");
    private static final String username = System.getenv("USERNAME");
    private List<Connection> connectionPool;
    private List<Connection> usedConnections = new ArrayList<>();
    private static int INITIAL_POOL_SIZE = 10;

    public BasicConnectionPoolUtil(String URL, String USERNAME, String PASSWORD, List<Connection> POOL) {
    }

    public static BasicConnectionPoolUtil create() throws SQLException {
        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);

        for(int i = 0; i < INITIAL_POOL_SIZE; i++){
            pool.add(createConnection());
        }

        return new BasicConnectionPoolUtil(url, username, password, pool);
    }

    @Override
    public Connection getConnection() {
        Connection connection = connectionPool.remove(connectionPool.size() - 1);
        usedConnections.add(connection);
        return connection;
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }

    private static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url,username,password);
    }

    public int getSize(){
        return connectionPool.size() + usedConnections.size();
    }
}
