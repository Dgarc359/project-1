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
    private String url;
    private String password;
    private String username;
    private List<Connection> connectionPool;
    private List<Connection> usedConnections = new ArrayList<>();
    private static int INITIAL_POOL_SIZE = 10;

    public BasicConnectionPoolUtil(String url, String username, String password, List<Connection> pool) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.connectionPool = pool;
    }

    public static BasicConnectionPoolUtil create(String url, String username, String password) throws SQLException {
        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);

        for(int i = 0; i < INITIAL_POOL_SIZE; i++){
            pool.add(createConnection(url, username, password));
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

    private static Connection createConnection(String url, String username, String password) throws SQLException {
        return DriverManager.getConnection(url,username,password);
    }

    public void shutdown() throws SQLException {
        usedConnections.forEach(this::releaseConnection);
        for (Connection c : connectionPool){
            c.close();
        }
    }

    public int getSize(){
        return connectionPool.size() + usedConnections.size();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
