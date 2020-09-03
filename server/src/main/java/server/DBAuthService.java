package server;

import java.sql.*;

public class DBAuthService implements AuthService {

    private class UserData {
        String login;
        String password;
        String nickname;

        public UserData(String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }
    }

    private static Connection connection;
    private static Statement stmt;

    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:mainDB.db");
        stmt = connection.createStatement();

    }

    private static void disconnect() {
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        String sql = String.format("SELECT nickname FROM main where login = '%s' and password = '%s'", login, password);
        try {
            connect();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getString("nickname");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
        return null;
    }

    public DBAuthService() {
    }

    @Override
    public boolean registration(String login, String password, String nickname) {
        boolean res = false;
        try {
            connect();
            String sql = String.format("INSERT INTO main (login, password, nickname) VALUES ('%s', '%s', '%s')", login, password, nickname);
            res = stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
        return !res;
    }

    @Override
    public boolean changeNickname(String login, String password, String newNickname) {
        boolean res = false;
        try {
            connect();
            String nickname = getNicknameByLoginAndPassword(login, password);
            if (newNickname != null) {
                System.out.println(nickname);
                System.out.println(newNickname);
                String sql = String.format("UPDATE main SET nickname = '%s' WHERE nickname = '%s' ", newNickname, nickname);
                System.out.println(sql);
                //тут проблема
                res = stmt.execute(sql);
                System.out.println(res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
        System.out.println(!res);
        return !res;
    }
}
