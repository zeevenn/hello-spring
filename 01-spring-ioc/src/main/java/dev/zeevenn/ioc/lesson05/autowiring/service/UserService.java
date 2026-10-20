package dev.zeevenn.ioc.lesson05.autowiring.service;

/**
 * 用户查询接口，只声明业务能力；依赖注入方式由各实现类自行决定。
 */
public interface UserService {

    String findName();
}
