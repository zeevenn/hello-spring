package dev.zeevenn.ioc.lesson03.annotationconfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * AppConfig
 */
@Configuration
@ComponentScan("dev.zeevenn.ioc.lesson03.annotationconfig") // 相当于 <context:component-scan base-package="" />
@PropertySource("classpath:lesson03/annotationconfig/database.properties") // 相当于 <context:property-placeholder location="" />
public class AppConfig {

}
