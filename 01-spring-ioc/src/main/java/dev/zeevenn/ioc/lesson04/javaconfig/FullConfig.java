package dev.zeevenn.ioc.lesson04.javaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * proxyBeanMethods 默认是 true：Spring 会增强配置类，拦截 @Bean 方法间的调用。
 * 在本例的默认配置下，userService() 内调用 userDao() 会取得容器中的同一个 DAO，
 * 而不是再次执行方法体中的 new UserDao()。
 * 只有 Spring 管理的增强配置对象才有这种行为，手动 new FullConfig() 不会产生代理。
 */
@Configuration
public class FullConfig {

    @Bean
    public UserDao userDao() {
        return new UserDao();
    }

    @Bean
    public UserService userService() {
        return new UserService(userDao());
    }
}
