package dev.zeevenn.ioc.lesson04.javaconfig;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.Clock;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

class JavaConfigTest {

    @Test
    @DisplayName("@Bean：显式注册对象，通过方法参数注入依赖")
    void beanMethodsRegisterAndWireObjects() {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            UserService service = context.getBean("userService", UserService.class);

            assertSame(context.getBean("userDao"), service.getUserDao());
            assertEquals("Ada", service.findName());
        }
    }

    @Test
    @DisplayName("@Bean 自定义名称：将 JDK 的 Clock 注册为 applicationClock")
    void beanMethodRegistersLibraryObjectWithExplicitName() {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            Clock clock = context.getBean("applicationClock", Clock.class);

            assertEquals(ZoneOffset.UTC, clock.getZone());
        }
    }

    @Test
    @DisplayName("proxyBeanMethods=true：方法间调用取得容器中的 DAO")
    void proxiedBeanMethodCallUsesManagedDao() {
        try (var context = new AnnotationConfigApplicationContext(FullConfig.class)) {
            UserDao dao = context.getBean(UserDao.class);
            UserService service = context.getBean(UserService.class);

            assertSame(dao, service.getUserDao());
        }
    }

    @Test
    @DisplayName("proxyBeanMethods=false：方法间调用额外创建一个 DAO")
    void unproxiedBeanMethodCallCreatesAnotherDao() {
        try (var context = new AnnotationConfigApplicationContext(LiteConfig.class)) {
            UserDao dao = context.getBean(UserDao.class);
            UserService service = context.getBean(UserService.class);

            assertNotSame(dao, service.getUserDao());
        }
    }
}
