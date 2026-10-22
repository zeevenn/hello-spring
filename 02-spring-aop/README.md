# AOP 学习目录

## 学习顺序

| 顺序 | 子包 | 内容 |
| ---- | ---- | ---- |
| 01 | `lesson01.staticproxy` | 静态代理日志示例 |
| 02 | `lesson02.dynamicproxy` | JDK 动态代理日志示例 |
| 03 | `lesson03.logging` | Spring AOP 日志示例 |

## 第一课：静态代理练习

这一部分不依赖 Spring，通过普通 Java 对象练习静态代理：代理负责记录日志，真实对象负责处理查询业务。

| 文件 | 当前状态 |
| ---- | -------------- |
| `UserService` | 声明 `findNameById(int id)` |
| `UserServiceImpl` | 返回 `"Ada" + id`，模拟按 ID 查询 |
| `UserServiceProxy` | 保存真实对象，调用前记录参数，委托查询，正常返回后记录并返回结果 |
| `StaticProxyApp` | 手动组装真实对象和代理，通过代理查询 ID 为 10 的用户 |

运行 `StaticProxyApp.main()`，可以看到：

```text
[前置日志] 调用 UserService.findNameById，id=10
[返回日志] UserService.findNameById 返回：Ada10
Ada10
```

返回日志位于业务调用之后，业务方法抛出异常时不会执行这条日志。此练习直接运行 `main` 观察效果，暂不添加测试。

## 第二课：JDK 动态代理日志练习

本课不依赖 Spring，也不需要添加新依赖。在 `DynamicProxyApp` 中直接创建代理，日志和真实对象的方法调用由独立的 `LoggingInvocationHandler` 处理。

| 文件 | 作用 |
| ---- | -------------- |
| `UserService` | 声明接口方法 `findNameById(int id)` |
| `UserServiceImpl` | 返回 `"Ada" + id`；ID 小于等于 0 时抛出业务异常 |
| `LoggingInvocationHandler` | 保存真实对象，在 `invoke` 中组织反射调用、返回结果、异常传播和四种日志 |
| `DynamicProxyApp` | 直接通过 `Proxy.newProxyInstance` 创建代理，演示正常查询和异常查询 |

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

`lesson03.logging` 演示：业务类只负责查询，日志统一由切面添加。

| 文件 | 作用 |
| ---- | ---- |
| `UserService` | 目标对象，`findName()` 返回 `Ada` |
| `LoggingAspect` | 切面，定义方法执行前和正常返回后的日志 |
| `AppConfig` | 扫描组件，通过 `@EnableAspectJAutoProxy` 开启 AOP |
| `LoggingApp` | 可以直接运行的 `main` 入口 |
| `LoggingTest` | 一个简单测试，确认代理存在且查询结果正确 |

建议先看 `UserService`，再看 `LoggingAspect` 和 `AppConfig`，最后运行示例。

## 先认识三个概念

- **切面（Aspect）**：集中放置日志等公共逻辑的类，本例是 `LoggingAspect`。
- **切入点（Pointcut）**：决定匹配哪些方法，本例是 `UserService.findName()`。
- **通知（Advice）**：匹配后执行什么、何时执行，本例是 `@Before` 和 `@AfterReturning` 标注的方法。

切入点表达式：

```java
execution(* dev.zeevenn.aop.lesson03.logging.UserService.findName())
```

`execution` 表示匹配方法执行，`*` 表示任意返回类型，完整方法名指定目标，`()` 表示无参数。

`@Before` 在方法执行前打印日志；`@AfterReturning` 在方法正常返回后打印日志，并通过 `returning = "result"` 获取返回值。它只记录结果，不修改结果；方法抛出异常时，返回通知不会运行。

## 运行

在 IDEA 中运行 `LoggingApp.main()`，可以看到：

```text
[前置日志] 调用 UserService.findName()
[返回日志] UserService.findName() 返回：Ada
查询结果：Ada
```

从项目根目录运行测试：

```bash
mvn -pl 02-spring-aop test
```

测试只验证代理存在和业务结果，日志直接在控制台观察。

## 配置说明

- `@Aspect` 声明切面，`@Component` 将切面注册到容器，`@EnableAspectJAutoProxy` 开启自动代理。
- 使用容器提供的 `UserService` 调用方法，代理会执行通知；手动 `new UserService()` 不会自动执行这些通知。
- 本模块添加 `spring-aop` 和 `aspectjweaver` 依赖，后者提供 AspectJ 注解及切入点表达式解析能力。本例使用 Spring 代理，不需要配置 AspectJ 编译器或 Java Agent。

参考 Spring 官方文档：[启用 AOP](https://docs.spring.io/spring-framework/reference/core/aop/ataspectj/aspectj-support.html)、[通知](https://docs.spring.io/spring-framework/reference/core/aop/ataspectj/advice.html)。
