package dev.zeevenn.aop.lesson02.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 调用处理器：代理对象收到方法调用后，JDK 会将调用转交给 invoke。
 * 与静态代理不同，这里通过一个统一入口处理方法调用，不必逐个实现业务方法。
 */
public class LoggingInvocationHandler implements InvocationHandler {

    // 保存真实业务对象；使用 Object，使处理器也能用于其他业务接口的实现对象。
    private final Object target;

    public LoggingInvocationHandler(Object target) {
        this.target = target;
    }

    /**
     * @param proxy JDK 生成的代理对象，不是真实业务对象
     * @param method 本次调用的接口方法，例如 findNameById
     * @param args 本次调用的参数；无参方法可能传入 null
     * @return 业务结果；基本类型包装为对应包装类，void 方法返回 null
     * @throws Throwable 允许方法调用过程中产生的异常继续向外传播
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // nanoTime 用于计算时间间隔；本例耗时包含前置、返回或异常日志的执行时间。
        long start = System.nanoTime();
        logBefore(method, args);

        try {
            // method 是接口方法，通过反射在真实对象上执行；args 是本次调用的实参。
            // 接收者必须是 target，若传入 proxy，会再次进入当前 invoke，造成递归。
            Object result = method.invoke(this.target, args);
            // 业务方法正常返回才会执行；抛出异常时会直接进入下面的 catch。
            logSuccess(method, result);
            // 反射统一返回 Object，例如 int 返回 Integer，代理随后拆箱为 int。
            // void 方法的 result 为 null，也可以直接返回，代理会忽略这个返回值。
            return result;
        } catch (InvocationTargetException e) {
            // Method.invoke 会把业务方法抛出的异常包装成 InvocationTargetException。
            // getCause() 取出原始业务异常，例如这里的 IllegalArgumentException。
            Throwable cause = e.getCause();
            logFailure(method, cause);
            // 日志只负责记录，异常继续抛给调用方，保留业务方法原有的异常行为。
            throw cause;
        } finally {
            // 正常返回或业务异常都会执行，纳秒差值交给辅助方法换算并记录。
            logDuration(method, System.nanoTime() - start);
        }
    }

    // 以下四个方法都是自己定义的私有辅助方法，不是 InvocationHandler 提供的方法。
    // invoke 组织调用流程，这些方法集中维护日志格式。
    private void logBefore(Method method, Object[] args) {
        // 无参调用的 args 可能为 null，可以统一显示为空数组 []。
        String parameters = args == null ? "[]" : Arrays.toString(args);
        System.out.println("[调用前] " + method.getName() + "，参数：" + parameters);
    }

    private void logSuccess(Method method, Object result) {
        System.out.println("[正常返回] " + method.getName() + "，结果：" + result);
    }

    private void logFailure(Method method, Throwable cause) {
        System.out.println("[异常] " + method.getName() + "，原因：" + cause);
    }

    private void logDuration(Method method, long elapsedNanos) {
        // 将纳秒换算为毫秒；用浮点数除法保留不足一毫秒的部分。
        double elapsedMillis = elapsedNanos / 1_000_000.0;
        System.out.println("[执行结束] " + method.getName() + "，耗时：" + elapsedMillis);
    }
}
