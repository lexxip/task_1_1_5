package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        String queryTxt = "create table users (" +
                        "id bigint not null auto_increment," +
                        " name varchar(20) not null," +
                        " lastName varchar(20) not null," +
                        " age tinyint not null," +
                        " primary key (id)" +
                        ") engine = InnoDB default charset = utf8mb4 collate = utf8mb4_0900_ai_ci";

        Session hibernateConnection = Util.getHibernateConnection().openSession();
        try {
            Query query = hibernateConnection.createSQLQuery(queryTxt);
            Transaction tx1 = hibernateConnection.beginTransaction();
            query.executeUpdate();
            tx1.commit();
            System.out.println("Таблица пользователей успешно создана");
        } catch (Exception e) {
            System.err.println("Не удалось создать таблицу");
        } finally {
            hibernateConnection.close();
        }
    }

    @Override
    public void dropUsersTable() {
        String queryTxt = "drop table if exists users";

        Session hibernateConnection = Util.getHibernateConnection().openSession();
        try {
            Query query = hibernateConnection.createSQLQuery(queryTxt);
            Transaction tx1 = hibernateConnection.beginTransaction();
            query.executeUpdate();
            tx1.commit();
            System.out.println("Таблица пользователей удалена");
        } catch (Exception e) {
            System.err.println("Не удалось удалить таблицу");
        } finally {
            hibernateConnection.close();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session hibernateConnection = Util.getHibernateConnection().openSession();
        try {
            Transaction tx1 = hibernateConnection.beginTransaction();
            hibernateConnection.save(new User(name, lastName, age));
            tx1.commit();
        } catch (Exception e) {
                System.err.println("Не удалось произвести запись в таблицу");
        } finally {
            hibernateConnection.close();
        }
        System.out.printf("User с именем – %s добавлен в базу данных.\n", name);
    }

    @Override
    public void removeUserById(long id) {
        Session hibernateConnection = Util.getHibernateConnection().openSession();
        try {
            Transaction tx1 = hibernateConnection.beginTransaction();
            hibernateConnection.delete((User) hibernateConnection.load(User.class, id));
            tx1.commit();
            System.out.printf("User с id – %s удален из базы данных\n", id);
        } catch (EntityNotFoundException e) {
            System.err.printf("User с id – %s отсутствует в базе данных.\n", id);
        } catch (Exception e) {
            System.err.println("Не удалось получить доступ к таблице");
        } finally {
            hibernateConnection.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        Session hibernateConnection = Util.getHibernateConnection().openSession();
        try {
            return (List<User>) hibernateConnection.createQuery("From User").list();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Не удалось получить доступ к таблице");
        } finally {
            hibernateConnection.close();
        }
        return null;
    }

    @Override
    public void cleanUsersTable() {
        Session hibernateConnection = Util.getHibernateConnection().openSession();
        try {
            Query query = hibernateConnection.createQuery("delete from User");
            Transaction tx1 = hibernateConnection.beginTransaction();
            query.executeUpdate();
            tx1.commit();
            System.out.println("Таблица пользователей очищена");
        } catch (Exception e) {
            System.err.println("Не удалось получить доступ к таблице");
        } finally {
            hibernateConnection.close();
        }
    }
}
