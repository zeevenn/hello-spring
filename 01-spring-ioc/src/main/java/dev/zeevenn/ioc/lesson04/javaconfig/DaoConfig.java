package dev.zeevenn.ioc.lesson04.javaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 将 DAO 的配置拆到独立配置类，由 AppConfig 通过 @Import 引入。
 */
@Configuration
public class DaoConfig {

    // 默认以方法名 userDao 作为 Bean 名。
    @Bean
    public UserDao userDao() {
        return new UserDao();
    }
}
