package dev.zeevenn.ioc.lesson04.javaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 反例对照：关闭方法代理后，直接调用 userDao() 就是普通 Java 方法调用。
 * 本例的方法体每次都会 new，因此服务拿到的 DAO 不同于容器注册的 DAO，
 * 服务内部额外创建的 DAO 也不会单独注册为 Bean。
 * 注意区分直接调用方法和从容器取对象：本例反复 getBean 仍返回容器中的同一实例。
 * 改法见 AppConfig：通过 @Bean 方法参数注入依赖，避免直接调用另一个 @Bean 方法。
 */
@Configuration(proxyBeanMethods = false)
public class LiteConfig {

    @Bean
    public UserDao userDao() {
        return new UserDao();
    }

    @Bean
    public UserService userService() {
        return new UserService(userDao());
    }
}
