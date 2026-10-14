package dev.zeevenn.ioc.lesson03.annotationconfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * AppConfig
 */
@Configuration
@ComponentScan("dev.zeevenn.ioc.lesson03.annotationconfig")
@PropertySource("classpath:lesson03/annotationconfig/database.properties")
public class AppConfig {

}
