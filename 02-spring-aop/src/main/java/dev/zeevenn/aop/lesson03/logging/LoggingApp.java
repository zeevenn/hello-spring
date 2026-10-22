package dev.zeevenn.aop.lesson03.logging;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 可以直接在 IDEA 中运行 main，观察日志和查询结果。
 */
public class LoggingApp {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            // 从容器拿到代理对象，调用方法时 Spring 才能执行相应的通知。
            // 手动 new UserService() 得到的普通对象不会自动执行本课的日志通知。
            UserService service = context.getBean(UserService.class);

            System.out.println("查询结果：" + service.findName());
        }
    }
}
