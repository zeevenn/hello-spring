package dev.zeevenn.ioc.lesson04.javaconfig;

/**
 * 普通 Java 类：没有 @Service，也能由 @Bean 创建并交给 Spring 管理。
 * 必需依赖仍通过构造器传入，字段可以声明为 final。
 */
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public String findName() {
        return this.userDao.findName();
    }

    public UserDao getUserDao() {
        return this.userDao;
    }
}
