package dev.zeevenn.tx.lesson01.transfer;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ComponentScan("dev.zeevenn.tx.lesson01.transfer")
@PropertySource("database.properties")
@EnableAspectJAutoProxy
public class AppConfig {

    public DriverManagerDataSource dataSource(
            @Value("${database.driver}") String driverClassName,
            @Value("${database.url}") String url,
            @Value("${database.username}") String username,
            @Value("${database.password}") String password) {
        // 创建 DriverManagerDataSource，设置驱动类名、URL、用户名和密码。
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        // TODO：使用 dataSource 创建 JdbcTemplate，并注册为 Bean。
        throw new UnsupportedOperationException("TODO：配置 JdbcTemplate");
    }

    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        // TODO：使用 dataSource 创建 DataSourceTransactionManager，并注册为 Bean。
        // 事务管理器和 JdbcTemplate 必须使用同一个 DataSource，才能协调同一数据库的操作。
        throw new UnsupportedOperationException("TODO：配置事务管理器");
    }
}
