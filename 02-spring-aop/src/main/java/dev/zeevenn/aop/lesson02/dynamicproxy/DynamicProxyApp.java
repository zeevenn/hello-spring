package dev.zeevenn.aop.lesson02.dynamicproxy;

import java.lang.reflect.Proxy;

/**
 * 示例入口：直接创建 JDK 动态代理，将日志和业务调用交给独立的处理器，无需 Spring。
 */
public class DynamicProxyApp {

    public static void main(String[] args) {
        // 真实对象负责业务，处理器保存它，并负责后续的日志和反射调用。
        UserService target = new UserServiceImpl();
        LoggingInvocationHandler handler = new LoggingInvocationHandler(target);

        // JDK 在运行时生成实现 UserService 接口的代理类，并创建代理对象。
        // 可以把生成的类理解为：class $Proxy0 implements UserService。
        // $Proxy0 和 UserServiceImpl 都实现 UserService，但两者之间没有继承关系。
        // 因此代理可以转换为 UserService；强转为 UserServiceImpl 会抛出 ClassCastException。
        // 代理通过处理器委托真实对象执行业务，委托关系不等于继承关系。
        UserService proxy = (UserService) Proxy.newProxyInstance(
                target.getClass().getClassLoader(), // 类加载器：用于加载生成的代理类。
                target.getClass().getInterfaces(), // 接口数组：本例取目标类直接实现的接口。
                handler // 调用处理器：代理的方法调用会交给它的 invoke 方法。
        );

        // 调用链：proxy.findNameById → handler.invoke → target.findNameById。
        // 直接调用 target 的方法不会经过处理器，也就不会执行这些代理日志。
        String name = proxy.findNameById(10);
        System.out.println("查询结果：" + name);

        // 非法 ID 用于观察异常日志；处理器会把原始业务异常继续抛到这里。
        try {
            proxy.findNameById(-1);
        } catch (IllegalArgumentException e) {
            System.out.println("调用方捕获异常：" + e.getMessage());
        }
    }
}
