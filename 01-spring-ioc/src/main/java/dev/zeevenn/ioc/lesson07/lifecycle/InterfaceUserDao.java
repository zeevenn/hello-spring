package dev.zeevenn.ioc.lesson07.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口方式：实现 Spring 的初始化和销毁回调接口。
 * 可以了解这种写法；一般优先使用注解或配置方式，减少业务类对 Spring 接口的依赖。
 */
public class InterfaceUserDao implements InitializingBean, DisposableBean {

    private final List<String> events = new ArrayList<>();

    public InterfaceUserDao() {
        this.events.add("constructor");
    }

    // 方法名表示“属性已设置完成”，此时可以进行初始化工作。
    @Override
    public void afterPropertiesSet() {
        this.events.add("afterPropertiesSet");
    }

    @Override
    public void destroy() {
        this.events.add("destroy");
    }

    public List<String> getEvents() {
        return List.copyOf(this.events);
    }
}
