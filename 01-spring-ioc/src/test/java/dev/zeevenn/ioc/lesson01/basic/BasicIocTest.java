package dev.zeevenn.ioc.lesson01.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BasicIocTest {

    @Test
    @DisplayName("手动组装：通过构造器注入 DAO 并查询")
    void manualAssemblyReturnsName() {
        UserDao userDao = new UserDao();
        UserService userService = new UserService(userDao);

        assertEquals("Ada", userService.findName());
    }

    @Test
    @DisplayName("容器组装：加载 XML 并完成依赖注入")
    void containerAssemblyReturnsName() {
        try (var context = new ClassPathXmlApplicationContext("lesson01/basic/beans.xml")) {
            UserService userService = context.getBean("userService", UserService.class);

            assertEquals("Ada", userService.findName());
        }
    }

    @Test
    @DisplayName("Bean 获取：按名称、类型、名称加类型获取同一实例")
    void lookupMethodsReturnSameBean() {
        try (var context = new ClassPathXmlApplicationContext("lesson01/basic/beans.xml")) {
            Object byName = context.getBean("userService");
            UserService byType = context.getBean(UserService.class);
            UserService byNameAndType = context.getBean("userService", UserService.class);

            assertSame(byName, byType);
            assertSame(byType, byNameAndType);
        }
    }

    @Test
    @DisplayName("默认单例：同一容器重复获取同一个 Bean 会复用实例")
    void singletonBeanIsReused() {
        try (var context = new ClassPathXmlApplicationContext("lesson01/basic/beans.xml")) {
            UserService first = context.getBean("userService", UserService.class);
            UserService second = context.getBean("userService", UserService.class);

            assertSame(first, second);
        }
    }

    @Test
    @DisplayName("Bean 获取：名称不存在时抛出异常")
    void missingBeanThrowsException() {
        try (var context = new ClassPathXmlApplicationContext("lesson01/basic/beans.xml")) {
            assertThrows(NoSuchBeanDefinitionException.class,
                    () -> context.getBean("missingService"));
        }
    }
}
