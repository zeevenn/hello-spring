package dev.zeevenn.aop.lesson02.dynamicproxy;

/**
 * JDK 动态代理通过接口暴露业务方法，生成的代理对象也实现这个接口。
 */
public interface UserService {

    /**
     * 按 ID 查询用户名。
     * @throws IllegalArgumentException ID 小于等于 0 时抛出
     */
    String findNameById(int id);
}
