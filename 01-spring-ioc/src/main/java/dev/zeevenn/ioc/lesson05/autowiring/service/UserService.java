package dev.zeevenn.ioc.lesson05.autowiring.service;

import dev.zeevenn.ioc.lesson01.basic.UserDao;

/**
 * UserService
 */
public interface UserService {

    UserDao setUserDao(UserDao userDao);

    String findName();
}
