package dev.zeevenn.aop.lesson02.dynamicproxy;

/**
 * 真实业务对象：只负责查询，不处理代理和日志。
 */
public class UserServiceImpl implements UserService {

    @Override
    public String findNameById(int id) {
        // 模拟业务校验失败，便于观察异常如何经过反射和代理传回调用方。
        if (id <= 0) {
            throw new IllegalArgumentException("查询用户 id 必须大于 0");
        }

        // 用固定名称模拟查询，日志统一由调用处理器添加。
        return "Ada" + id;
    }
}
