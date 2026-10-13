package dev.zeevenn.ioc.lesson01.basic;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public String findName() {
        return this.userDao.findName();
    }
}
