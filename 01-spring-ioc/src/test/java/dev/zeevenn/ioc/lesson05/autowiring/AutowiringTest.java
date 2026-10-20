package dev.zeevenn.ioc.lesson05.autowiring;

import dev.zeevenn.ioc.lesson05.autowiring.service.UserService;
import dev.zeevenn.ioc.lesson05.autowiring.service.impl.PrimaryUserService;
import dev.zeevenn.ioc.lesson05.autowiring.service.impl.QualifierUserService;
import dev.zeevenn.ioc.lesson05.autowiring.service.impl.ResourceUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

// 三个服务实现同时存在，分别获取具体实现，再通过统一的 UserService 接口调用。
class AutowiringTest {

    @Test
    @DisplayName("@Primary：默认注入数据库实现")
    void primarySelectsDatabaseDao() {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            UserService service = context.getBean(PrimaryUserService.class);

            assertEquals("Ada (database)", service.findName());
        }
    }

    @Test
    @DisplayName("@Qualifier：构造器参数指定内存实现")
    void qualifierSelectsMemoryDao() {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            UserService service = context.getBean(QualifierUserService.class);

            assertEquals("Ada (memory)", service.findName());
        }
    }

    @Test
    @DisplayName("@Resource：Setter 按名称注入内存实现")
    void resourceSelectsMemoryDaoByExplicitName() {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            UserService service = context.getBean(ResourceUserService.class);

            assertEquals("Ada (memory)", service.findName());
        }
    }
}
