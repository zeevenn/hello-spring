package dev.zeevenn.ioc.lesson02.xmlconfig;

/**
 * InstanceUserServiceFactory
 */
public class InstanceUserServiceFactory {

    public UserService create(UserDao userDao) {
        return new UserService(userDao);
    }
}
