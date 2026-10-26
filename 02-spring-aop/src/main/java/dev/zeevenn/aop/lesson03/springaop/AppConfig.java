package dev.zeevenn.aop.lesson03.springaop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan("dev.zeevenn.aop.lesson03.springaop")
// 根据切面的匹配规则为 Bean 创建代理，调用时由代理执行通知。
// 开启基于 @Aspect 注解的 Spring AOP 自动代理支持
@EnableAspectJAutoProxy
public class AppConfig {
}
