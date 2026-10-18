package dev.zeevenn.ioc.lesson07.lifecycle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LifecycleTest {

    @Test
    @DisplayName("注解方式：先构造再初始化，关闭容器后调用销毁方法")
    void annotationsHandleInitializationAndDestruction() {
        UserDao dao;
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            dao = context.getBean("userDao", UserDao.class);

            assertEquals(List.of("constructor", "@PostConstruct"), dao.getEvents());
        } // try 结束时自动调用 context.close()，触发单例 Bean 的销毁回调。

        assertEquals(List.of("constructor", "@PostConstruct", "@PreDestroy"), dao.getEvents());
    }

    @Test
    @DisplayName("配置方式：@Bean 指定 initMethod 和 destroyMethod")
    void beanConfigurationSelectsLifecycleMethods() {
        ConfiguredUserDao dao;
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            dao = context.getBean(ConfiguredUserDao.class);

            assertEquals(List.of("constructor", "init"), dao.getEvents());
        }

        assertEquals(List.of("constructor", "init", "cleanup"), dao.getEvents());
    }

    @Test
    @DisplayName("接口方式：Spring 调用 afterPropertiesSet 和 destroy")
    void springInterfacesReceiveLifecycleCallbacks() {
        InterfaceUserDao dao;
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            dao = context.getBean(InterfaceUserDao.class);

            assertEquals(List.of("constructor", "afterPropertiesSet"), dao.getEvents());
        }

        assertEquals(List.of("constructor", "afterPropertiesSet", "destroy"), dao.getEvents());
    }

    @Test
    @DisplayName("原型 Bean：获取时初始化，关闭容器时不会自动调用销毁方法")
    void prototypeDoesNotReceiveAutomaticDestructionCallback() {
        UserDao dao;
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            dao = context.getBean("prototypeUserDao", UserDao.class);
        }

        assertEquals(List.of("constructor", "@PostConstruct"), dao.getEvents());
    }
}
