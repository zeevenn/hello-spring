# AOP 学习目录

## 学习顺序

| 顺序 | 子包                    | 内容                 |
| ---- | ----------------------- | -------------------- |
| 01   | `lesson01.staticproxy`  | 静态代理日志示例     |
| 02   | `lesson02.dynamicproxy` | JDK 动态代理日志示例 |
| 03   | `lesson03.springaop`    | Spring AOP 日志示例  |

## 第一课：静态代理练习

这一部分不依赖 Spring，通过普通 Java 对象练习静态代理：代理负责记录日志，真实对象负责处理查询业务。

| 文件               | 当前状态                                                         |
| ------------------ | ---------------------------------------------------------------- |
| `UserService`      | 声明 `findNameById(int id)`                                      |
| `UserServiceImpl`  | 返回 `"Ada" + id`，模拟按 ID 查询                                |
| `UserServiceProxy` | 保存真实对象，调用前记录参数，委托查询，正常返回后记录并返回结果 |
| `StaticProxyApp`   | 手动组装真实对象和代理，通过代理查询 ID 为 10 的用户             |

运行 `StaticProxyApp.main()`，可以看到：

```text
[前置日志] 调用 UserService.findNameById，id=10
[返回日志] UserService.findNameById 返回：Ada10
Ada10
```

返回日志位于业务调用之后，业务方法抛出异常时不会执行这条日志。此练习直接运行 `main` 观察效果，暂不添加测试。

## 第二课：JDK 动态代理日志练习

本课不依赖 Spring，也不需要添加新依赖。在 `DynamicProxyApp` 中直接创建代理，日志和真实对象的方法调用由独立的 `LoggingInvocationHandler` 处理。

| 文件                       | 作用                                                                   |
| -------------------------- | ---------------------------------------------------------------------- |
| `UserService`              | 声明接口方法 `findNameById(int id)`                                    |
| `UserServiceImpl`          | 返回 `"Ada" + id`；ID 小于等于 0 时抛出业务异常                        |
| `LoggingInvocationHandler` | 保存真实对象，在 `invoke` 中组织反射调用、返回结果、异常传播和四种日志 |
| `DynamicProxyApp`          | 直接通过 `Proxy.newProxyInstance` 创建代理，演示正常查询和异常查询     |

本课直接运行 `main` 观察效果，暂不添加测试。

运行 `DynamicProxyApp.main()`，输出如下（耗时单位为毫秒，每次不同）：

```text
[调用前] findNameById，参数：[10]
[正常返回] findNameById，结果：Ada10
[执行结束] findNameById，耗时：...
查询结果：Ada10
[调用前] findNameById，参数：[-1]
[异常] findNameById，原因：java.lang.IllegalArgumentException: 查询用户 id 必须大于 0
[执行结束] findNameById，耗时：...
调用方捕获异常：查询用户 id 必须大于 0
```

- 静态代理需要手写实现业务接口的代理类；JDK 动态代理在运行时生成实现指定接口的代理类。
- 代理收到业务调用后，会进入 `InvocationHandler.invoke(proxy, method, args)`，其中 `proxy` 是代理对象，`method` 是接口方法，`args` 是实参。
- 处理器通过反射调用真实对象；不要再次调用代理本身，以免递归。
- `logBefore`、`logSuccess`、`logFailure`、`logDuration` 是自己定义的私有辅助方法，用于集中维护日志格式，并非 JDK 提供的方法。
- `Method.invoke` 会用 `InvocationTargetException` 包装业务异常。处理器取出原始异常，记录后继续抛出；`finally` 中无论成功还是失败都会记录耗时。
- `Method.invoke` 的返回类型是 `Object`：基本类型返回对应的包装对象，代理再拆箱；`void` 方法返回 `null`，处理器可以直接返回，代理会忽略该值。

参考 Java 官方文档：[Proxy](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/reflect/Proxy.html)、[InvocationHandler](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/reflect/InvocationHandler.html)。

## 第三课：Spring AOP 简单日志

`lesson03.springaop` 演示：业务类只负责查询，日志统一由切面添加。

| 文件                       | 作用                                                                   |
| -------------------------- | ---------------------------------------------------------------------- |
| `service/UserService`      | 按 ID 查询，返回 `"Ada" + id`                                          |
| `service/OrderService`     | 返回订单数量，演示同一条规则覆盖其他业务类和基本类型返回值             |
| `pointcut/CommonPointcuts` | 按业务层集中管理公共切入点                                             |
| `aspect/LoggingAspect`     | 引用公共规则，分别记录方法、参数、返回值、异常及耗时                       |
| `AppConfig`                | 扫描组件，通过 `@EnableAspectJAutoProxy` 开启 AOP                      |
| `LoggingApp`               | 可以直接运行的 `main` 入口                                             |
| `LoggingTest`              | 三个简单测试，验证代理、结果、异常传播和切入点边界 |

建议先看 `CommonPointcuts`，再看 `LoggingAspect` 和两个业务类，最后运行 `LoggingApp`。

### 切入点的统一管理

按职责组织第三课：

```text
lesson03/springaop/
├── AppConfig.java
├── LoggingApp.java
├── pointcut/CommonPointcuts.java
├── aspect/LoggingAspect.java
└── service/
    ├── UserService.java
    └── OrderService.java
```

业务类统一放在 `service` 包及其子包，切入点按层匹配，类名和方法名可以按业务需要命名。新增业务类放入该层后，已有切面会应用同一条规则。多个切面需要相同范围时，共同引用 `CommonPointcuts` 中的命名切入点。

## 结合示例理解 AOP 概念

| 概念                      | 含义与本例对应关系                                                                   |
| ------------------------- | ------------------------------------------------------------------------------------ |
| 切面（Aspect）            | 集中实现跨越多个业务类的需求，例如日志或事务；本例为 `LoggingAspect`                 |
| 连接点（Join point）      | 程序执行中可被增强的位置；Spring AOP 中表示方法执行，本例调用 `findNameById(int id)` |
| 切入点（Pointcut）        | 匹配连接点的规则；本例为 `CommonPointcuts.serviceMethods()`                          |
| 通知（Advice）            | 在匹配的连接点上执行的动作；本例由四个通知方法分别记录日志和耗时            |
| 目标对象（Target object） | 被增强的真实业务对象；本例为执行查询的 `UserService` 对象                            |
| AOP 代理（AOP proxy）     | 组织通知和业务调用的代理对象；本例容器返回的是 CGLIB 代理                            |
| 织入（Weaving）           | 将切面与应用对象关联以实现增强的过程；Spring AOP 使用运行时代理完成                  |

连接点是方法执行这个位置，切入点是选择这些位置的规则；通知定义匹配后何时执行什么操作。规则可以匹配多个类的方法，不要求这些类有共同的业务父类。

`CommonPointcuts` 先声明小范围规则，再组合为业务层的公共方法规则：

```java
@Pointcut("execution(public * *(..))")
public void publicMethods() {
}

@Pointcut("within(dev.zeevenn.aop.lesson03.springaop.service..*)")
public void inServiceLayer() {
}

@Pointcut("publicMethods() && inServiceLayer()")
public void serviceMethods() {
}
```

`execution(public * *(..))` 匹配任意返回类型、任意名称、任意参数的 `public` 方法。`within` 限定声明方法的类型所在范围，`service..*` 包含业务包及其所有子包，`&&` 表示同时满足两条规则。

声明切入点的方法体留空，解析器读取方法上的 `@Pointcut` 配置。跨类引用使用完整类名和方法名：

```java
@Before("dev.zeevenn.aop.lesson03.springaop.pointcut.CommonPointcuts.serviceMethods()")

@AfterReturning(
    pointcut = "dev.zeevenn.aop.lesson03.springaop.pointcut.CommonPointcuts.serviceMethods()",
    returning = "result")
```

上面的注解分别用于前置通知和返回通知的方法。日志通过 `JoinPoint.getSignature().toShortString()` 获取含类名的方法签名，通过 `getArgs()` 获取实参。

`@AfterReturning` 通过 `returning = "result"` 将返回值传入通知的 `Object result` 参数，以接收不同方法的结果。它只记录结果，不修改结果；方法抛出异常时，返回通知不会运行。

Spring AOP 的五种通知及其时机：

| 通知              | 何时执行                                                                                                    |
| ----------------- | ----------------------------------------------------------------------------------------------------------- |
| `@Before`         | 方法执行前；正常完成通知后继续调用业务方法，通知抛异常可中断调用                                            |
| `@AfterReturning` | 方法正常返回后                                                                                              |
| `@AfterThrowing`  | 方法抛出异常退出时                                                                                          |
| `@After`          | 方法结束时，无论正常返回还是抛出异常，类似 `finally`                                                        |
| `@Around`         | 包围方法调用；通过 `ProceedingJoinPoint.proceed()` 继续执行后续调用，也可以直接返回或抛出异常来跳过后续调用 |

本例演示 `@Before`、`@AfterReturning`、`@AfterThrowing` 和 `@Around`，分别负责入参、返回值、异常和耗时。

`@AfterThrowing` 的 `throwing = "cause"` 将业务异常传入通知的 `Throwable cause` 参数，通知记录后，原异常会继续向调用方传播。正常返回和异常退出分别触发 `@AfterReturning`、`@AfterThrowing`，同一次目标方法执行只进入其中一个分支。

`@Around` 接收 `ProceedingJoinPoint`，通过 `proceed()` 继续执行后续通知和目标方法，返回业务结果。在 `finally` 中统计耗时，因此正常和异常调用都会记录执行时间。这里的耗时包含内部通知的执行时间。

通过本例代理调用时，可以按下面的顺序观察通知：

```text
环绕通知记录开始时间 → 前置通知 → 业务方法
    正常返回 → 返回通知 → 环绕通知记录耗时 → 调用方得到结果
    抛出异常 → 异常通知 → 环绕通知记录耗时 → 调用方捕获异常
```

按官方建议，选择足以实现需求的通知类型：读取返回值使用返回通知，记录异常使用异常通知，需要包围整个调用统计耗时时使用环绕通知。

## 运行

在 IDEA 中运行 `LoggingApp.main()`，可以看到以下输出（耗时每次不同）：

```text
[调用前] UserService.findNameById(..)，参数：[10]
[正常返回] UserService.findNameById(..)，结果：Ada10
[执行结束] UserService.findNameById(..)，耗时：... ms
查询结果：Ada10
[调用前] OrderService.countOrders()，参数：[]
[正常返回] OrderService.countOrders()，结果：2
[执行结束] OrderService.countOrders()，耗时：... ms
订单数量：2
[调用前] UserService.findNameById(..)，参数：[-1]
[异常] UserService.findNameById(..)，原因：java.lang.IllegalArgumentException: 查询用户 id 必须大于 0
[执行结束] UserService.findNameById(..)，耗时：... ms
调用方捕获异常：查询用户 id 必须大于 0
```

从项目根目录运行测试：

```bash
mvn -pl 02-spring-aop test
```

测试验证两个服务获得代理、业务结果和异常不变，以及业务层外的方法不匹配，日志直接在控制台观察。

## 配置说明

- `@Aspect` 声明切面，`@Component` 将切面注册到容器，`@EnableAspectJAutoProxy` 开启自动代理。
- 使用容器提供的 `UserService` 调用方法，代理会执行通知；手动 `new UserService()` 不会自动执行这些通知。
- 本模块添加 `spring-aop` 和 `aspectjweaver` 依赖，后者提供 AspectJ 注解及切入点表达式解析能力。本例使用 Spring 代理，不需要配置 AspectJ 编译器或 Java Agent。

参考官方文档：[启用 AOP](https://docs.spring.io/spring-framework/reference/core/aop/ataspectj/aspectj-support.html)、[共享切入点](https://docs.spring.io/spring-framework/reference/core/aop/ataspectj/pointcuts.html#_sharing_named_pointcut_definitions)、[通知](https://docs.spring.io/spring-framework/reference/core/aop/ataspectj/advice.html)。
