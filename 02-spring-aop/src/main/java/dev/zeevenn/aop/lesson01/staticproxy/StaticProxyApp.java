package dev.zeevenn.aop.lesson01.staticproxy;

/**
 * 练习入口：手动组装真实对象和代理，再通过代理调用业务方法。
 */
public class StaticProxyApp {

    public static void main(String[] args) {
        // 创建真实业务对象。
        UserServiceImpl target = new UserServiceImpl();
        // 创建代理对象，将真实对象传给代理。
        UserServiceProxy proxy = new UserServiceProxy(target);
        // 通过代理查询名称，打印结果并观察日志顺序。
        System.out.println(proxy.findNameById(10));
    }
}
