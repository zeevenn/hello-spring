package dev.zeevenn.ioc.lesson04.javaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.time.Clock;

/**
 * Java 显式配置：@Bean 的作用类似 XML 的 {@code <bean>}，方法返回的对象由容器管理。
 * 本例不需要 @ComponentScan，因为所有业务 Bean 都由 @Bean 声明。
 * 当方法之间没有直接调用时，可以关闭配置类方法代理，并用参数表达依赖。
 * AppConfig、FullConfig、LiteConfig 分别作为入口加载，避免相同 Bean 名发生冲突。
 */
@Configuration(proxyBeanMethods = false)
// 引入 DaoConfig 及其中的 @Bean 定义，启动时只需加载 AppConfig。
@Import(DaoConfig.class)
public class AppConfig {

    // Spring 调用此工厂方法时，先从容器解析 UserDao，再传入参数，无需 @Autowired。
    // UserDao 由引入的 DaoConfig 注册，与当前配置中的 Bean 一样可以注入。
    // 参数注入在 proxyBeanMethods 为 true 或 false 时都适用，不依赖配置类方法代理。
    @Bean
    public UserService userService(UserDao userDao) {
        return new UserService(userDao);
    }

    // Clock 是 JDK 提供的类，无法修改源码给它加 @Component，但可以用 @Bean 注册。
    // 第三方库对象也适用这种方式；显式指定名称后，Bean 名是 applicationClock，而非 clock。
    @Bean("applicationClock")
    public Clock clock() {
        return Clock.systemUTC();
    }
}
