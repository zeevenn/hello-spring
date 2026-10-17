package dev.zeevenn.ioc.lesson06.scope;

/**
 * 单例服务直接注入原型 DAO：创建服务时注入一次，之后一直使用该对象。
 */
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserDao getUserDao() {
        return this.userDao;
    }
}
