package dev.zeevenn.ioc.lesson06.scope;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

class ScopeTest {

    @Test
    @DisplayName("singleton：反复获取同一个 Bean，复用同一实例")
    void singletonReusesInstance() {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            UserDao first = context.getBean("singletonUserDao", UserDao.class);
            UserDao second = context.getBean("singletonUserDao", UserDao.class);

            assertSame(first, second);
        }
    }

    @Test
    @DisplayName("prototype：每次获取同一个 Bean，创建不同实例")
    void prototypeCreatesNewInstances() {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            UserDao first = context.getBean("prototypeUserDao", UserDao.class);
            UserDao second = context.getBean("prototypeUserDao", UserDao.class);

            assertNotSame(first, second);
        }
    }

    @Test
    @DisplayName("单例直接依赖原型：注入一次后，始终使用同一个对象")
    void directInjectionKeepsOriginalInstance() {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            UserService service = context.getBean(UserService.class);

            assertSame(service.getUserDao(), service.getUserDao());
        }
    }

    @Test
    @DisplayName("ObjectProvider：单例 DAO 复用，原型 DAO 每次创建新对象")
    void providerRespectsBeanScopes() {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            ProviderUserService service = context.getBean(ProviderUserService.class);

            assertSame(service.getSingletonUserDao(), service.getSingletonUserDao());
            assertNotSame(service.getPrototypeUserDao(), service.getPrototypeUserDao());
        }
    }
}
