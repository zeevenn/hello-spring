package dev.zeevenn.aop.lesson02.logging;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
// 扫描本课的 UserService 和 LoggingAspect，将它们注册为 Bean。
@ComponentScan("dev.zeevenn.aop.lesson02.logging")
// 开启对 @Aspect 的支持，让 Spring 为需要增强的 Bean 创建代理。
@EnableAspectJAutoProxy
public class AppConfig {
}
