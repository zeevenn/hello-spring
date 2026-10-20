package dev.zeevenn.ioc.lesson06.scope;

import org.springframework.beans.factory.ObjectProvider;

/**
 * 单例服务通过 ObjectProvider 向容器获取 DAO。
 * 两个入口分别获取原型和单例 DAO，比较相同的 getObject() 调用在不同作用域下的结果。
 */
public class ProviderUserService {

    private final ObjectProvider<UserDao> prototypeUserDaoProvider;
    private final ObjectProvider<UserDao> singletonUserDaoProvider;

    public ProviderUserService(ObjectProvider<UserDao> prototypeUserDaoProvider,
            ObjectProvider<UserDao> singletonUserDaoProvider) {
        this.prototypeUserDaoProvider = prototypeUserDaoProvider;
        this.singletonUserDaoProvider = singletonUserDaoProvider;
    }

    // 目标是 prototype，每次获取新对象。
    public UserDao getPrototypeUserDao() {
        return this.prototypeUserDaoProvider.getObject();
    }

    // 目标是 singleton，即使反复 getObject()，仍获取同一个对象。
    public UserDao getSingletonUserDao() {
        return this.singletonUserDaoProvider.getObject();
    }
}
