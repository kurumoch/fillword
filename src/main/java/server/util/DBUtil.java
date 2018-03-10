package server.util;

import client.models.Level;

import java.sql.*;

public class DBUtil {

    private static Connection connection;

    static {
        try {
            connection = new MySqlUtil().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static int getLevelCount() throws SQLException {
        Statement ps = connection.createStatement();
        ResultSet rs = ps.executeQuery("SELECT COUNT(*) FROM LEVEL");
        rs.next();
        return rs.getInt(1);
    }

    public static Level getLevel(int n) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT FIELD, WORDS FROM LEVEL WHERE ID=?");
        ps.setInt(1, n + 1);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int wordsToSolve = rs.getString(2).split(",").length;
        return new Level(rs.getString(1), wordsToSolve, n);
    }

}
