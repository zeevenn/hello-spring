package dev.zeevenn.ioc.lesson02.xmlconfig;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

class XmlConfigTest {

    @Test
    @DisplayName("构造器引用注入：通过构造器注入 DAO 并查询")
    void constructorInjectionReturnsName() {
        try (var context = new ClassPathXmlApplicationContext("lesson02/xmlconfig/constructor.xml")) {
            UserService service = context.getBean("userService", UserService.class);

            assertEquals("Ada", service.findName());
        }
    }

    @Test
    @DisplayName("DAO 外部属性注入：读取数据库配置并将连接超时转换为 int")
    void databasePropertiesAreInjectedIntoDao() {
        try (var context = new ClassPathXmlApplicationContext("lesson02/xmlconfig/database.xml")) {
            UserDao dao = context.getBean("userDao", UserDao.class);

            assertEquals("jdbc:mysql://localhost:3306/hello_spring", dao.getUrl());
            assertEquals("demo_user", dao.getUsername());
            assertEquals("demo_password", dao.getPassword());
            assertEquals(20, dao.getConnectionTimeoutSeconds());
        }
    }

    @Test
    @DisplayName("Setter 引用注入：通过 setUserDao 注入 DAO 并查询")
    void setterInjectionReturnsName() {
        try (var context = new ClassPathXmlApplicationContext("lesson02/xmlconfig/setter.xml")) {
            SetterUserService service = context.getBean("setterUserService", SetterUserService.class);

            assertEquals("Ada", service.findName());
        }
    }

    @Test
    @DisplayName("静态工厂：调用静态 create 方法创建服务并注入 DAO")
    void staticFactoryCreatesService() {
        try (var context = new ClassPathXmlApplicationContext("lesson02/xmlconfig/static-factory.xml")) {
            UserService service = context.getBean("userService", UserService.class);

            assertEquals("Ada", service.findName());
        }
    }

    @Test
    @DisplayName("实例工厂：调用工厂 Bean 的 create 方法创建服务并注入 DAO")
    void instanceFactoryCreatesService() {
        try (var context = new ClassPathXmlApplicationContext("lesson02/xmlconfig/instance-factory.xml")) {
            UserService service = context.getBean("userService", UserService.class);

            assertEquals("Ada", service.findName());
        }
    }
}
