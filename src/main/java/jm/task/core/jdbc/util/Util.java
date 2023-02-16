package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {

    private static SessionFactory hibernateConnection;

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

    public static SessionFactory getHibernateConnection() {
        if (hibernateConnection != null) {
            return hibernateConnection;
        }

        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/task_1_1_4");
        properties.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        properties.setProperty("hibernate.connection.username", "root");
        properties.setProperty("hibernate.connection.password", "root");

        try {
            hibernateConnection = new Configuration()
                    .setProperties(properties)
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Не удалось установить соединение с базой данных");
            System.exit(0);
        }
        return hibernateConnection;
    }

}
