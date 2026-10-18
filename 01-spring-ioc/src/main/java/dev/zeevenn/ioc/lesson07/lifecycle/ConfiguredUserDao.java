package dev.zeevenn.ioc.lesson07.lifecycle;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置方式：普通 Java 方法，由 @Bean 的 initMethod、destroyMethod 指定。
 * 适合已有初始化和清理方法、但无法修改源码添加注解的库对象。
 */
public class ConfiguredUserDao {

    private final List<String> events = new ArrayList<>();

    public ConfiguredUserDao() {
        this.events.add("constructor");
    }

    public void init() {
        this.events.add("init");
    }

    public void cleanup() {
        this.events.add("cleanup");
    }

    public List<String> getEvents() {
        return List.copyOf(this.events);
    }
}
