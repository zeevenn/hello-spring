package dev.zeevenn.ioc.lesson04.javaconfig;

/**
 * 普通 Java 类：没有 @Repository，由配置类中的 @Bean 方法显式注册。
 */
public class UserDao {

    public String findName() {
        return "Ada";
    }
}
