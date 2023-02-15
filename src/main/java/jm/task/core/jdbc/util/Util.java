package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {

    private Util() {}

    public static Connection getConnection(String url, String user, String password) {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Не удалось установить соединение с базой данных");
            System.exit(0);
        }
        return connection;
    }

}
