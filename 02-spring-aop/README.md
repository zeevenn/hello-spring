# AOP 学习目录

## 学习顺序

| 顺序 | 子包 | 内容 |
| ---- | ---- | ---- |
| 01 | `lesson01.staticproxy` | 静态代理练习（已完成委托调用，日志待补充） |
| 02 | `lesson02.logging` | Spring AOP 日志示例 |

## 第一课：静态代理练习

这一部分不依赖 Spring，通过普通 Java 对象练习静态代理。目前已完成接口、业务方法和代理委托调用，日志部分留作练习。

| 文件 | 当前状态 |
| ---- | -------------- |
| `UserService` | 声明 `findNameById(int id)` |
| `UserServiceImpl` | 返回 `"Ada" + id`，模拟按 ID 查询 |
| `UserServiceProxy` | 保存真实对象并委托查询，调用前和正常返回后的日志待补充 |
| `StaticProxyApp` | 手动组装真实对象和代理，通过代理查询 ID 为 10 的用户 |

运行 `StaticProxyApp.main()`，当前输出 `Ada10`。后续在代理方法中补充日志，再观察调用顺序。此练习暂不添加测试。

## 第二课：Spring AOP 简单日志

`lesson02.logging` 演示：业务类只负责查询，日志统一由切面添加。

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
execution(* dev.zeevenn.aop.lesson02.logging.UserService.findName())
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
