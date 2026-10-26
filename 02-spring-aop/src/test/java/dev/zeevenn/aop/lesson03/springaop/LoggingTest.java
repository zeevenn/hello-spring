package dev.zeevenn.aop.lesson03.springaop;

import dev.zeevenn.aop.lesson03.springaop.service.OrderService;
import dev.zeevenn.aop.lesson03.springaop.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoggingTest {

    @Test
    @DisplayName("公共切入点：不同业务类都获得代理，返回结果保持不变")
    void proxyLogsMethodCallAndPreservesResult() {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            UserService service = context.getBean(UserService.class);

            // 确认容器返回了 AOP 代理；调用方法时，在控制台观察通知日志。
            assertTrue(AopUtils.isAopProxy(service));
            assertEquals("Ada10", service.findNameById(10));

            OrderService orderService = context.getBean(OrderService.class);
            assertTrue(AopUtils.isAopProxy(orderService));
            assertEquals(2, orderService.countOrders());
        }
    }

    @Test
    @DisplayName("切入点边界：业务层方法匹配，应用入口不匹配")
    void pointcutStaysWithinServiceLayer() throws NoSuchMethodException {
        var pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(
                "dev.zeevenn.aop.lesson03.springaop.pointcut.CommonPointcuts.serviceMethods()");

        assertTrue(pointcut.matches(UserService.class.getMethod("findNameById", int.class), UserService.class));
        assertFalse(pointcut.matches(LoggingApp.class.getMethod("main", String[].class), LoggingApp.class));
    }

    @Test
    @DisplayName("异常通知和环绕通知：记录日志后，调用方仍收到原始业务异常")
    void advicesPreserveBusinessException() {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            UserService service = context.getBean(UserService.class);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class, () -> service.findNameById(-1));
            assertEquals("查询用户 id 必须大于 0", exception.getMessage());
        }
    }
}
