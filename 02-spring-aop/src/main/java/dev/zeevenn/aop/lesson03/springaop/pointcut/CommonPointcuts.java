package dev.zeevenn.aop.lesson03.springaop.pointcut;

import org.aspectj.lang.annotation.Pointcut;

/**
 * 统一管理可复用的切入点，按业务层命名，由日志等切面共同引用。
 */
public class CommonPointcuts {

    @Pointcut("execution(public * *(..))")
    public void publicMethods() {
    }

    // service..* 包含 service 包及其所有子包中的类型。
    @Pointcut("within(dev.zeevenn.aop.lesson03.springaop.service..*)")
    public void inServiceLayer() {
    }

    // && 组合规则：只匹配业务层的 public 方法。
    // public 允许其他切面用完整类名引用；空方法体仅为切入点提供名称。
    @Pointcut("publicMethods() && inServiceLayer()")
    public void serviceMethods() {
    }
}
