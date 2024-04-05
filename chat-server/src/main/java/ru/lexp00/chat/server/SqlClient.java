package ru.lexp00.chat.server;

import java.sql.*;

public class SqlClient {
    private static Connection connection;
    private static Statement statement;

    public synchronized static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:chat-server/chat-server.db");
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static void disconnect() {
        try{
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public synchronized static String getNickname(String login, String password) {
        String query = String.format("select nickname from clients where login='%s' and password='%s'", login, password);
        try (ResultSet resultSet = statement.executeQuery(query)){
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
