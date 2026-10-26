package dev.zeevenn.aop.lesson03.springaop;

import dev.zeevenn.aop.lesson03.springaop.service.OrderService;
import dev.zeevenn.aop.lesson03.springaop.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 可以直接在 IDEA 中运行 main，观察日志和查询结果。
 */
public class LoggingApp {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            // AOP 代理（AOP proxy）是容器返回给调用方的对象，目标对象负责实际业务。
            // 本例 UserService 没有实现接口，Spring 使用 CGLIB 生成它的子类作为代理。
            // 因此这里可以用 UserService 接收，与第二课的 JDK 接口代理不同。
            // 调用代理时，代理组织通知和目标方法的执行；手动 new 的对象不会自动获得这些通知。
            UserService service = context.getBean(UserService.class);

            System.out.println("查询结果：" + service.findNameById(10));

            OrderService orderService = context.getBean(OrderService.class);
            System.out.println("订单数量：" + orderService.countOrders());

            try {
                service.findNameById(-1);
            } catch (IllegalArgumentException e) {
                System.out.println("调用方捕获异常：" + e.getMessage());
            }
        }
    }
}
