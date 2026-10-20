package dev.zeevenn.ioc.lesson06.scope;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * 用同一个 UserDao 类注册两种作用域的 Bean，比较对象是否复用。
 */
@Configuration
public class AppConfig {

    // 默认是 singleton，无需额外写 @Scope("singleton")。
    // 在同一个容器中，反复获取这个 Bean 会复用同一实例，不是整个 JVM 只能有一个对象。
    @Bean
    public UserDao singletonUserDao() {
        return new UserDao();
    }

    // prototype（原型）：每次向容器获取这个 Bean，都会创建一个新实例。
    @Bean
    @Scope("prototype")
    public UserDao prototypeUserDao() {
        return new UserDao();
    }

    // 服务自身默认是单例，创建时直接注入一次原型对象。
    @Bean
    public UserService userService(@Qualifier("prototypeUserDao") UserDao userDao) {
        return new UserService(userDao);
    }

    // 同样调用 provider.getObject()，是否创建新对象取决于目标 Bean 的作用域。
    @Bean
    public ProviderUserService providerUserService(
            @Qualifier("prototypeUserDao") ObjectProvider<UserDao> prototypeUserDaoProvider,
            @Qualifier("singletonUserDao") ObjectProvider<UserDao> singletonUserDaoProvider) {
        return new ProviderUserService(prototypeUserDaoProvider, singletonUserDaoProvider);
    }
}
