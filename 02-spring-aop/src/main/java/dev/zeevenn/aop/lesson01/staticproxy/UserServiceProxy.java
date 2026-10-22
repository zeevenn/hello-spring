package dev.zeevenn.aop.lesson01.staticproxy;

/**
 * 静态代理：和真实对象实现同一个接口，在调用真实对象前后添加日志。
 * 本练习使用普通 Java 对象，不需要 Spring 注解或容器。
 */
public class UserServiceProxy implements UserService {

    // 定义字段，保存被代理的 UserService 对象。
    private UserService target;

    public UserServiceProxy(UserService target) {
        // 保存构造器传入的真实对象。
        this.target = target;
    }

    @Override
    public String findNameById(int id) {
        return this.target.findNameById(id);
    }
}
