package dev.zeevenn.ioc.lesson07.lifecycle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {

    // 默认单例，Spring 自动识别对象上的 @PostConstruct、@PreDestroy。
    @Bean
    public UserDao userDao() {
        return new UserDao();
    }

    // 指定普通方法作为初始化和销毁回调，无需给 DAO 添加生命周期注解。
    @Bean(initMethod = "init", destroyMethod = "cleanup")
    public ConfiguredUserDao configuredUserDao() {
        return new ConfiguredUserDao();
    }

    // Spring 自动识别 InitializingBean、DisposableBean，不需要额外指定方法名。
    @Bean
    public InterfaceUserDao interfaceUserDao() {
        return new InterfaceUserDao();
    }

    // 原型 Bean 获取时也会初始化，但容器关闭时不会自动调用其销毁回调。
    // 原型对象持有的资源需要由使用方自行释放。
    @Bean
    @Scope("prototype")
    public UserDao prototypeUserDao() {
        return new UserDao();
    }
}
