# IoC 学习目录

## 学习顺序

| 顺序 | 子包                        | 练习内容                                           | 计划测试                                      |
| ---- | --------------------------- | -------------------------------------------------- | --------------------------------------------- |
| 01   | `lesson01.basic`            | 手动组装与容器组装、Bean 获取                      | `BasicIocTest`：查询结果、Bean 获取           |
| 02   | `lesson02.xmlconfig`        | 构造器、Setter、工厂方法、数据库属性配置           | `XmlConfigTest`：注入结果、属性读取与类型转换 |
| 03   | `lesson03.annotationconfig` | 组件标记、扫描、构造器注入                         | `AnnotationConfigTest`：组件注册、缺失依赖    |
| 04   | `lesson04.javaconfig`       | `@Bean`、参数注入、`proxyBeanMethods`              | `JavaConfigTest`：实例身份比较                |
| 05   | `lesson05.autowiring`       | 多候选 Bean、`@Primary`、`@Qualifier`、`@Resource` | `AutowiringTest`：歧义与候选选择              |
| 06   | `lesson06.scope`            | 单例、原型、单例依赖原型                           | `ScopeTest`：实例复用与按需获取               |
| 07   | `lesson07.lifecycle`        | 初始化、销毁、容器关闭                             | `LifecycleTest`：回调顺序与销毁范围           |
| 08   | `lesson08.extension`        | Bean 定义与实例的后处理器                          | `ExtensionTest`：定义修改与实例处理           |

## 目录

```text
src/
├── main/
│   ├── java/dev/zeevenn/ioc/lessonNN/<主题>/
│   └── resources/lessonNN/<主题>/
└── test/
    ├── java/dev/zeevenn/ioc/lessonNN/<主题>/
    └── resources/lessonNN/<主题>/
```

## 约定

- 主代码和测试使用同名包：`dev.zeevenn.ioc.lessonNN.<主题>`。
- 每个练习只扫描自己的主题包。
- 笔记示例的包名调整时，同步修改 `import`、XML 的 `class` 和扫描范围。
- XML 使用主题路径，例如 `lesson01/basic/beans.xml`；测试专用配置使用不同文件名。

## 运行测试

父 POM 统一配置 JUnit Jupiter 和 Maven Surefire，各课以测试类作为主要练习入口。测试方法使用 `@Test`，通过断言验证结果；IDEA 重新加载 Maven 项目后，可以点击测试类或方法旁的运行按钮。

在项目根目录运行全部测试：

```bash
mvn test
```

只运行 basic：

```bash
mvn -pl 01-spring-ioc -Dtest=BasicIocTest test
```

## Java 显式配置：lesson04.javaconfig

与 lesson03 的组件扫描不同，本课的 `UserDao`、`UserService` 都是普通 Java 类，由配置类中的 `@Bean` 方法显式创建和注册，无需 `@ComponentScan`。

| 配置类 | 组装方式 | 服务中的 DAO 与容器中的 DAO |
| ------ | -------- | --------------------------- |
| `AppConfig` | `proxyBeanMethods = false`，通过 `@Bean` 方法参数注入 | 同一实例 |
| `FullConfig` | 默认 `proxyBeanMethods = true`，直接调用另一个 `@Bean` 方法 | 同一实例 |
| `LiteConfig` | `proxyBeanMethods = false`，直接调用另一个 `@Bean` 方法（反例） | 不同实例 |

- 默认 Bean 名为 `@Bean` 方法名；也可以用 `@Bean("applicationClock")` 显式命名。本课使用 JDK 的 `Clock` 演示注册无法修改源码添加组件注解的库对象，第三方类也适用。
- Spring 调用 `@Bean` 方法时，会从容器解析其参数，再由方法将依赖传给业务对象的构造器；方法参数无需标注 `@Autowired`。
- `proxyBeanMethods` 控制配置类的 `@Bean` 方法调用是否经过拦截。本课先比较直接调用方法和从容器取对象的区别：在默认配置下，关闭方法代理后反复 `getBean` 仍取得容器中的同一实例。实例复用的规则留到 lesson06 再展开。
- `LiteConfig.userDao()` 每次执行都会 `new` 一个 DAO，所以直接调用会创建额外对象。依赖改用方法参数传入即可避免这个问题，见 `AppConfig`。
- 三个配置类分别加载；不要一起扫描或注册，否则相同 Bean 名会发生冲突。手动 `new FullConfig()` 也不会得到 Spring 增强的配置对象。

从 `AppConfig` 开始，再运行 `JavaConfigTest` 的四个示例：注册与参数注入、注册库对象，以及方法代理开关的对照。通过 `assertSame` / `assertNotSame` 比较对象身份：

```bash
mvn -pl 01-spring-ioc -Dtest=JavaConfigTest test
```

参考 Spring 官方文档：[@Bean 与 @Configuration](https://docs.spring.io/spring-framework/reference/core/beans/java/basic-concepts.html)、[@Bean 方法参数与命名](https://docs.spring.io/spring-framework/reference/core/beans/java/bean-annotation.html)。
