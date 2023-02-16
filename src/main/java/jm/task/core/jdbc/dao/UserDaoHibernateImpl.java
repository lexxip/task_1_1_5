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

        Session session = Util.getHibernateConnection().openSession();
        Transaction tx1 = null;
        try {
            Query query = session.createSQLQuery(queryTxt);
            tx1 = session.beginTransaction();
            query.executeUpdate();
            tx1.commit();
            System.out.println("Таблица пользователей успешно создана");
        } catch (Exception e) {
            tx1.rollback();
            System.err.println("Не удалось создать таблицу");
        } finally {
            session.close();
        }
    }

    @Override
    public void dropUsersTable() {
        String queryTxt = "drop table if exists users";

        Session session = Util.getHibernateConnection().openSession();
        Transaction tx1 = null;
        try {
            Query query = session.createSQLQuery(queryTxt);
            tx1 = session.beginTransaction();
            query.executeUpdate();
            tx1.commit();
            System.out.println("Таблица пользователей удалена");
        } catch (Exception e) {
            tx1.rollback();
            System.err.println("Не удалось удалить таблицу");
        } finally {
            session.close();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = Util.getHibernateConnection().openSession();
        Transaction tx1 = null;
        try {
            tx1 = session.beginTransaction();
            session.save(new User(name, lastName, age));
            tx1.commit();
        } catch (Exception e) {
            tx1.rollback();
            System.err.println("Не удалось произвести запись в таблицу");
        } finally {
            session.close();
        }
        System.out.printf("User с именем – %s добавлен в базу данных.\n", name);
    }

    @Override
    public void removeUserById(long id) {
        Session session = Util.getHibernateConnection().openSession();
        Transaction tx1 = null;
        try {
            tx1 = session.beginTransaction();
            session.delete((User) session.load(User.class, id));
            tx1.commit();
            System.out.printf("User с id – %s удален из базы данных\n", id);
        } catch (EntityNotFoundException e) {
            tx1.rollback();
            System.err.printf("User с id – %s отсутствует в базе данных.\n", id);
        } catch (Exception e) {
            tx1.rollback();
            System.err.println("Не удалось получить доступ к таблице");
        } finally {
            session.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        Session session = Util.getHibernateConnection().openSession();
        try {
            return (List<User>) session.createQuery("From User").list();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Не удалось получить доступ к таблице");
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public void cleanUsersTable() {
        Session session = Util.getHibernateConnection().openSession();
        Transaction tx1 = null;
        try {
            Query query = session.createQuery("delete from User");
            tx1 = session.beginTransaction();
            query.executeUpdate();
            tx1.commit();
            System.out.println("Таблица пользователей очищена");
        } catch (Exception e) {
            tx1.rollback();
            System.err.println("Не удалось получить доступ к таблице");
        } finally {
            session.close();
        }
    }
}
