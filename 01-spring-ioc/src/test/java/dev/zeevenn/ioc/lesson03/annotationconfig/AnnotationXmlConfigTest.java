package dev.zeevenn.ioc.lesson03.annotationconfig;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * 注解 + XML：XML 配置组件扫描和属性文件，注解声明组件及注入点。
 */
class AnnotationXmlConfigTest {

    private static final String CONFIG_LOCATION = "lesson03/annotationconfig/beans.xml";

    @Test
    @DisplayName("注解 + XML：通过 XML 扫描 @Repository 和 @Service，不加载 Java 配置类")
    void xmlScanningRegistersComponentsWithoutJavaConfig() {
        try (var context = new ClassPathXmlApplicationContext(CONFIG_LOCATION)) {
            assertSame(context.getBean(UserDao.class), context.getBean("userDao"));
            assertSame(context.getBean(UserService.class), context.getBean("userService"));
            assertFalse(context.containsBean("appConfig"));
        }
    }

    @Test
    @DisplayName("注解 + XML：扫描服务后，自动通过单构造器注入 DAO 并查询")
    void singleConstructorInjectionReturnsName() {
        try (var context = new ClassPathXmlApplicationContext(CONFIG_LOCATION)) {
            UserService service = context.getBean(UserService.class);

            assertEquals("Ada", service.findName());
        }
    }

    @Test
    @DisplayName("注解 + XML：扫描服务后，通过 @Autowired 为字段注入 DAO 并查询")
    void fieldInjectionReturnsName() {
        try (var context = new ClassPathXmlApplicationContext(CONFIG_LOCATION)) {
            FieldUserService service = context.getBean(FieldUserService.class);

            assertEquals("Ada", service.findName());
        }
    }

    @Test
    @DisplayName("注解 + XML：扫描服务后，通过 @Autowired 调用 Setter 注入 DAO 并查询")
    void setterInjectionReturnsName() {
        try (var context = new ClassPathXmlApplicationContext(CONFIG_LOCATION)) {
            SetterUserService service = context.getBean(SetterUserService.class);

            assertEquals("Ada", service.findName());
        }
    }

    @Test
    @DisplayName("注解 + XML：XML 加载属性文件，@Value 注入数据库配置并转换类型")
    void xmlPropertiesAreInjectedIntoDao() {
        try (var context = new ClassPathXmlApplicationContext(CONFIG_LOCATION)) {
            UserDao dao = context.getBean(UserDao.class);

            assertEquals("jdbc:mysql://localhost:3306/hello_spring", dao.getUrl());
            assertEquals("demo_user", dao.getUsername());
            assertEquals("demo_password", dao.getPassword());
            assertEquals(20, dao.getConnectionTimeoutSeconds());
        }
    }
}
