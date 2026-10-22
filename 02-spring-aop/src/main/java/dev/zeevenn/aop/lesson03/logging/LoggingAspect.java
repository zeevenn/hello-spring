package dev.zeevenn.aop.lesson03.logging;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 切面：集中放置日志等可以应用于业务方法的公共逻辑。
 * 本课直接输出到控制台，便于观察通知执行的时机。
 */
@Aspect // 声明这是一个切面，内部定义通知及其匹配规则。
@Component // @Aspect 本身不会注册 Bean，还需要由 Spring 管理这个对象。
public class LoggingAspect {

    // @Before 是前置通知，在匹配的业务方法执行前运行。
    // execution(...) 是切入点表达式，用于指定哪些方法需要执行通知。
    // * 表示任意返回类型，后面是方法的完整名称，() 表示无参数。
    @Before("execution(* dev.zeevenn.aop.lesson03.logging.UserService.findName())")
    public void logBefore() {
        System.out.println("[前置日志] 调用 UserService.findName()");
    }

    // @AfterReturning 是返回通知，只在业务方法正常返回后运行，抛出异常时不会运行。
    // returning = "result" 将返回值传给下面同名的 result 参数。
    // 此处只读取结果打印日志，不改变业务方法的返回值。
    @AfterReturning(
            pointcut = "execution(* dev.zeevenn.aop.lesson03.logging.UserService.findName())",
            returning = "result")
    public void logAfterReturning(String result) {
        System.out.println("[返回日志] UserService.findName() 返回：" + result);
    }
}
