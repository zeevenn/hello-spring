package dev.zeevenn.ioc.lesson07.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.util.ArrayList;
import java.util.List;

/**
 * 注解方式：记录创建、初始化和销毁的顺序，不连接真实数据库。
 * 注解来自 jakarta.annotation，需要引入对应依赖。
 */
public class UserDao {

    private final List<String> events = new ArrayList<>();

    public UserDao() {
        this.events.add("constructor");
    }

    // 对象创建并完成依赖注入后，由 Spring 调用，可用于准备数据、检查配置。
    @PostConstruct
    public void init() {
        this.events.add("@PostConstruct");
    }

    // 容器关闭时，Spring 为已创建的单例 Bean 调用，可用于释放资源。
    @PreDestroy
    public void destroy() {
        this.events.add("@PreDestroy");
    }

    public List<String> getEvents() {
        return List.copyOf(this.events);
    }
}
