package server.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlUtil {

    private Connection connection;

    public MySqlUtil() throws SQLException,
            ClassNotFoundException {
        String hostName = "localhost";
        String dbName = "DB";
        String userName = "root";
        String password = "1";

        Class.forName("com.mysql.jdbc.Driver");
        String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName;

        connection = DriverManager.getConnection(connectionURL, userName,
                password);
    }

    public Connection getConnection() {
        return connection;
    }


}
