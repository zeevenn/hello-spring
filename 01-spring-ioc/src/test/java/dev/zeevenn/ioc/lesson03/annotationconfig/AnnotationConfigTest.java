package dev.zeevenn.ioc.lesson03.annotationconfig;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 注解 + Java 配置类
 */
class AnnotationConfigTest {

    private static final String BASE_PACKAGE = "dev.zeevenn.ioc.lesson03.annotationconfig";

    @Test
    @DisplayName("组件扫描：注册 @Repository 和 @Service，使用默认 Bean 名称")
    void scanningRegistersComponentsWithDefaultNames() {
        try (var context = new AnnotationConfigApplicationContext(BASE_PACKAGE)) {
            assertSame(context.getBean(UserDao.class), context.getBean("userDao"));
            assertSame(context.getBean(UserService.class), context.getBean("userService"));
        }
    }

    @Test
    @DisplayName("单构造器注入：不标注 @Autowired 也能注入 DAO 并查询")
    void singleConstructorInjectionReturnsName() {
        try (var context = new AnnotationConfigApplicationContext(BASE_PACKAGE)) {
            UserService service = context.getBean(UserService.class);

            assertEquals("Ada", service.findName());
        }
    }

    @Test
    @DisplayName("字段注入：通过 @Autowired 为字段注入 DAO 并查询")
    void fieldInjectionReturnsName() {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            FieldUserService service = context.getBean(FieldUserService.class);

            assertEquals("Ada", service.findName());
        }
    }

    @Test
    @DisplayName("Setter 注入：通过 @Autowired 调用 Setter 注入 DAO 并查询")
    void setterInjectionReturnsName() {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            SetterUserService service = context.getBean(SetterUserService.class);

            assertEquals("Ada", service.findName());
        }
    }

    @Test
    @DisplayName("@Value 属性注入：加载数据库配置，并将连接超时转换为 int")
    void databasePropertiesAreInjectedIntoDao() {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            UserDao dao = context.getBean(UserDao.class);

            assertEquals("jdbc:mysql://localhost:3306/hello_spring", dao.getUrl());
            assertEquals("demo_user", dao.getUsername());
            assertEquals("demo_password", dao.getPassword());
            assertEquals(20, dao.getConnectionTimeoutSeconds());
        }
    }

    @Test
    @DisplayName("组件标记：未扫描或显式注册时，注解类不会自动成为 Bean")
    void annotationsAloneDoNotRegisterComponents() {
        try (var context = new AnnotationConfigApplicationContext()) {
            context.refresh();

            assertThrows(NoSuchBeanDefinitionException.class,
                    () -> context.getBean(UserService.class));
            assertThrows(NoSuchBeanDefinitionException.class,
                    () -> context.getBean(UserDao.class));
        }
    }

    @Test
    @DisplayName("缺失依赖：只注册服务、遗漏 DAO 时，容器初始化失败")
    void missingDaoPreventsContextInitialization() {
        try (var context = new AnnotationConfigApplicationContext()) {
            // 显式注册服务而不注册 DAO，模拟扫描范围遗漏依赖的结果。
            context.register(UserService.class);

            UnsatisfiedDependencyException exception = assertThrows(
                    UnsatisfiedDependencyException.class, context::refresh);
            NoSuchBeanDefinitionException cause = assertInstanceOf(
                    NoSuchBeanDefinitionException.class, exception.getMostSpecificCause());

            assertEquals(UserDao.class, cause.getBeanType());
        }
    }
}
