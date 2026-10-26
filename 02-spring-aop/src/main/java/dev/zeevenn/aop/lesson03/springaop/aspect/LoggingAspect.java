package dev.zeevenn.aop.lesson03.springaop.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 切面（Aspect）：把分散在多个类中的同一种需求集中实现，例如日志或事务。
 * 这种跨越多个业务类的需求称为横切关注点，本例对业务层方法统一记录日志。
 *
 * 连接点（Join point）：程序执行中可以被增强的位置；Spring AOP 中表示方法执行。指的是执行过程中的位置，不是方法前后那几段通知代码。
 * 切入点（Pointcut）：匹配连接点的规则，集中在 CommonPointcuts 中管理。
 * 通知（Advice）：通知就是具体执行的增强逻辑，例如记录日志、检查权限。多个通知可以围绕同一次方法执行组成调用链。
 * 可以按“在哪里匹配（切入点）、何时做什么（通知）”理解一个切面的配置。
 */
@Aspect // 声明这是一个切面，内部定义通知及其匹配规则。
@Component // @Aspect 本身不会注册 Bean，还需要由 Spring 管理这个对象。
public class LoggingAspect {

    // @Before 是前置通知，在匹配的业务方法执行前运行。
    // 正常执行完通知后会继续调用业务方法；它不能通过返回值跳过业务调用，但抛异常可中断调用。
    @Before("dev.zeevenn.aop.lesson03.springaop.pointcut.CommonPointcuts.serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        // toShortString() 包含类名和方法名，getArgs() 提供实参。
        System.out.println("[调用前] " + joinPoint.getSignature().toShortString()
                + "，参数：" + Arrays.toString(joinPoint.getArgs()));
    }

    // @AfterReturning 是返回通知，只在业务方法正常返回后运行，抛出异常时不会运行。
    // returning = "result" 将返回值传给下面同名的 result 参数。
    // 用 Object 接收不同方法的返回值；基本类型会包装为对应的包装类。
    @AfterReturning(pointcut = "dev.zeevenn.aop.lesson03.springaop.pointcut.CommonPointcuts.serviceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        System.out.println("[正常返回] " + joinPoint.getSignature().toShortString() + "，结果：" + result);
    }

    // throwing = "cause" 把方法抛出的异常绑定到同名参数；Throwable 可以接收各种异常。
    // 异常通知正常结束后，原异常仍会继续传给调用方，无需在这里重新抛出。
    @AfterThrowing(
            pointcut = "dev.zeevenn.aop.lesson03.springaop.pointcut.CommonPointcuts.serviceMethods()",
            throwing = "cause")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable cause) {
        System.out.println("[异常] " + joinPoint.getSignature().toShortString() + "，原因：" + cause);
    }

    @Around("dev.zeevenn.aop.lesson03.springaop.pointcut.CommonPointcuts.serviceMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();
        try {
            // proceed() 继续执行后续通知和目标方法，并返回结果。
            // 环绕通知需要把结果返回给调用方；业务异常会直接向外传播。
            return joinPoint.proceed();
        } finally {
            // 无论正常返回还是抛出异常都统计耗时，包含内部通知的执行时间。
            double elapsedMillis = (System.nanoTime() - start) / 1_000_000.0;
            System.out.println("[执行结束] " + joinPoint.getSignature().toShortString()
                    + "，耗时：" + elapsedMillis + " ms");
        }
    }
}
