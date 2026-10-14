package dev.zeevenn.ioc.lesson02.xmlconfig;

/**
 * SetterUserService
 */
public class SetterUserService {

    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public String findName() {
        return this.userDao.findName();
    }
}
