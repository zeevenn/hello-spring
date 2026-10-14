package dev.zeevenn.ioc.lesson02.xmlconfig;

/**
 * StaticUserServiceFactory
 */
public class StaticUserServiceFactory {

    public static UserService create(UserDao userDao) {
        return new UserService(userDao);
    }
}
