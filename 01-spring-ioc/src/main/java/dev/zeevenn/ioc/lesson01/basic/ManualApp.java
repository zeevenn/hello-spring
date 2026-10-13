package dev.zeevenn.ioc.lesson01.basic;

/**
 * ManualApp
 */
public class ManualApp {

    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        UserService userService = new UserService(userDao);
        System.out.println(userService.findName());
    }
}
