package dev.zeevenn.aop.lesson01.staticproxy;

/**
 * 公共接口：业务实现类和代理类都实现它，调用方通过接口使用服务。
 */
public interface UserService {

    String findNameById(int id);
}
