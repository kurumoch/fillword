package server.util;

import client.models.Level;
import client.models.Pair;

import java.sql.*;
import java.util.ArrayList;

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

    public static Pair<String, String> getLevelInfo(int n) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT FIELD, WORDS FROM LEVEL WHERE ID=?");
        ps.setInt(1, n + 1);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return new Pair<>(rs.getString(1), rs.getString(2));
    }

    public static void writeResult(String nickname, int score, int n) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO SCORE (level_id, nickname, score) VALUES(?,?,?)");
        ps.setInt(1, n + 1);
        ps.setString(2, nickname);
        ps.setInt(3, score);
        ps.execute();
    }

    public static ArrayList<Pair<String, Integer>> getScores(int n) throws SQLException {
        ArrayList<Pair<String, Integer>> out = new ArrayList<>();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM SCORE WHERE LEVEL_ID=? ORDER BY SCORE DESC LIMIT 5");
        ps.setInt(1, n + 1);
        ResultSet rs = ps.executeQuery();
        while (rs.next())
            out.add(new Pair<>(rs.getString(3), rs.getInt(4)));
        return out;
    }

}
