package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {

    private static final String URL = "jdbc:mysql://localhost:3306/task_1_1_4";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static SessionFactory sessionFactory;

    private Util() {}

    public static Connection getConnection() {

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Не удалось установить соединение с базой данных");
            System.exit(0);
        }
        return connection;
    }

    public static SessionFactory getHibernateConnection() {
        if (sessionFactory != null) {
            return sessionFactory;
        }

        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/task_1_1_4");
        properties.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        properties.setProperty("hibernate.connection.username", "root");
        properties.setProperty("hibernate.connection.password", "root");

        try {
            sessionFactory = new Configuration()
                    .setProperties(properties)
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Не удалось установить соединение с базой данных");
            System.exit(0);
        }
        return sessionFactory;
    }

}
