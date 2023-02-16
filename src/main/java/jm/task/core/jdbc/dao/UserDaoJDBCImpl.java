package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private final String tableName = "users";

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String query = String.format("create table %s (" +
                        "id bigint not null auto_increment," +
                        " name varchar(20) not null," +
                        " lastName varchar(20) not null," +
                        " age tinyint not null," +
                        " primary key (id)" +
                        ") engine = InnoDB default charset = utf8mb4 collate = utf8mb4_0900_ai_ci",
                tableName);

        Connection connection = Util.getConnection();
        Statement statement = null;

        try {
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            statement.execute(query);
            connection.commit();
            System.out.println("Таблица успешно создана");
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    System.err.println("Транзакция отменена");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (e.getErrorCode() == 1050) {
                System.err.printf("Таблица \"%s\" уже существует\n", tableName);
            } else {
                e.printStackTrace();
            }
        } finally {
            try {
                statement.close();
                connection.setAutoCommit(true);
                connection.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void dropUsersTable() {
        String query = String.format("drop table task_1_1_4.%s", tableName);

        Connection connection = Util.getConnection();
        Statement statement = null;

        try  {
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            statement.execute(query);
            connection.commit();
            System.out.println("Таблица успешно удалена");
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    System.err.println("Транзакция отменена");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (e.getErrorCode() == 1051) {
                System.err.printf("Таблица \"%s\" не существует\n", tableName);
            } else {
                e.printStackTrace();
            }
        } finally {
            try {
                statement.close();
                connection.setAutoCommit(true);
                connection.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String query = String.format("insert into task_1_1_4.%s (name, lastName, age) values (?, ?, ?)", tableName);

        Connection connection = Util.getConnection();
        PreparedStatement preparedStatement = null;

        try  {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.execute();
            connection.commit();
            System.out.printf("User с именем – %s добавлен в базу данных.\n", name);
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    System.err.println("Транзакция отменена");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (e.getErrorCode() == 1146) {
                System.err.println("Попытка добавить запись в несуществующую таблицу");
            } else {
                e.printStackTrace();
            }
        } finally {
            try {
                preparedStatement.close();
                connection.setAutoCommit(true);
                connection.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void removeUserById(long id) {
        String query = String.format("delete from task_1_1_4.%s WHERE (id = ?)", tableName);

        Connection connection = Util.getConnection();
        PreparedStatement preparedStatement = null;

        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, id);
            System.out.printf((preparedStatement.executeUpdate() != 0 ? "User с id – %s удален из базы данных.\n" : "User с id – %s отсутствует в базе данных.\n"), id);
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    System.err.println("Транзакция отменена");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (e.getErrorCode() == 1146) {
                System.err.println("Попытка удаления записи из несуществующей таблицы");
            } else {
                e.printStackTrace();
            }
        } finally {
            try {
                preparedStatement.close();
                connection.setAutoCommit(true);
                connection.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<User> getAllUsers() {
        ResultSet resultSet = null;
        List<User> users = new ArrayList<>();
        String query = String.format("select * from task_1_1_4.%s", tableName);
        Connection connection = Util.getConnection();
        PreparedStatement preparedStatement = null;

        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery(query);
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                User user = new User(resultSet.getString("name"),
                        resultSet.getString("lastName"),
                        resultSet.getByte("age"));
                user.setId(id);
                users.add(user);
            }
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    System.err.println("Транзакция отменена");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (e.getErrorCode() == 1146) {
                System.err.println("Попытка прочитать записи из несуществующей таблицы");
            } else {
                e.printStackTrace();
            }
        } finally {
            try {
                preparedStatement.close();
                resultSet.close();
                connection.setAutoCommit(true);
                connection.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return users;
    }

    public void cleanUsersTable() {
        String query = String.format("delete from task_1_1_4.%s", tableName);

        Connection connection = Util.getConnection();
        Statement statement = null;

        try {
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            statement.execute(query);
            System.out.println("Таблица пользователей очищена");
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    System.err.println("Транзакция отменена");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (e.getErrorCode() == 1146) {
                System.err.println("Попытка очистить несуществующую таблицу");
            } else {
                e.printStackTrace();
            }
        } finally {
            try {
                statement.close();
                connection.setAutoCommit(true);
                connection.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
