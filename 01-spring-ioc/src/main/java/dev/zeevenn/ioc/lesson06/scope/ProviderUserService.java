package dev.zeevenn.ioc.lesson06.scope;

import org.springframework.beans.factory.ObjectProvider;

/**
 * 单例服务通过 ObjectProvider 向容器获取 DAO。
 * 两个入口分别获取原型和单例 DAO，比较相同的 getObject() 调用在不同作用域下的结果。
 */
public class ProviderUserService {

    private final ObjectProvider<UserDao> userDaoProvider;
    private final ObjectProvider<UserDao> singletonUserDaoProvider;

    public ProviderUserService(ObjectProvider<UserDao> userDaoProvider,
            ObjectProvider<UserDao> singletonUserDaoProvider) {
        this.userDaoProvider = userDaoProvider;
        this.singletonUserDaoProvider = singletonUserDaoProvider;
    }

    // 目标是 prototype，每次获取新对象。
    public UserDao getUserDao() {
        return this.userDaoProvider.getObject();
    }

    // 目标是 singleton，即使反复 getObject()，仍获取同一个对象。
    public UserDao getSingletonUserDao() {
        return this.singletonUserDaoProvider.getObject();
    }
}
