package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Petr", "Shiriaev", (byte) 10);
        userService.saveUser("Aleks", "Shiriaev", (byte) 46);
        userService.saveUser("Natalia", "Okulova", (byte) 46);
        userService.saveUser("Galina", "Shiriaeva", (byte) 80);
        List<User> users = userService.getAllUsers();
        users.forEach(System.out::println);
        userService.removeUserById(2);
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
