package dev.zeevenn.aop.lesson02.logging;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoggingTest {

    @Test
    @DisplayName("日志切面：通过代理调用业务方法，打印前后日志并保持查询结果")
    void proxyLogsMethodCallAndPreservesResult() {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            UserService service = context.getBean(UserService.class);

            // 确认容器返回了 AOP 代理；调用方法时，在控制台观察前置、返回两条日志。
            assertTrue(AopUtils.isAopProxy(service));
            assertEquals("Ada", service.findName());
        }
    }
}
