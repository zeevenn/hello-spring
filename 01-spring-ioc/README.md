# IoC 学习目录

## 学习顺序

| 顺序 | 子包                        | 练习内容                                           | 计划测试                                      |
| ---- | --------------------------- | -------------------------------------------------- | --------------------------------------------- |
| 01   | `lesson01.basic`            | 手动组装与容器组装、Bean 获取                      | `BasicIocTest`：查询结果、Bean 获取           |
| 02   | `lesson02.xmlconfig`        | 构造器、Setter、工厂方法、数据库属性配置           | `XmlConfigTest`：注入结果、属性读取与类型转换 |
| 03   | `lesson03.annotationconfig` | 组件标记、扫描、构造器注入                         | `AnnotationConfigTest`：组件注册、缺失依赖    |
| 04   | `lesson04.javaconfig`       | `@Bean`、`@Import`、参数注入、`proxyBeanMethods`   | `JavaConfigTest`：配置组合与实例身份比较      |
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
| `AppConfig` | `@Import(DaoConfig.class)` 引入 DAO 配置，`proxyBeanMethods = false`，通过 `@Bean` 方法参数注入 | 同一实例 |
| `FullConfig` | 默认 `proxyBeanMethods = true`，直接调用另一个 `@Bean` 方法 | 同一实例 |
| `LiteConfig` | `proxyBeanMethods = false`，直接调用另一个 `@Bean` 方法（反例） | 不同实例 |

- 默认 Bean 名为 `@Bean` 方法名；也可以用 `@Bean("applicationClock")` 显式命名。本课使用 JDK 的 `Clock` 演示注册无法修改源码添加组件注解的库对象，第三方类也适用。
- Spring 调用 `@Bean` 方法时，会从容器解析其参数，再由方法将依赖传给业务对象的构造器；方法参数无需标注 `@Autowired`。
- `@Import(DaoConfig.class)` 将 DAO 的配置组合进 `AppConfig`。启动时只需加载 `AppConfig.class`，就能获取并注入 `DaoConfig` 中声明的 `userDao`，不需要组件扫描。Java 的 `import` 语句只用于类名引用，Spring 的 `@Import` 则参与容器配置。
- `proxyBeanMethods` 控制配置类的 `@Bean` 方法调用是否经过拦截。本课先比较直接调用方法和从容器取对象的区别：在默认配置下，关闭方法代理后反复 `getBean` 仍取得容器中的同一实例。实例复用的规则留到 lesson06 再展开。
- `LiteConfig.userDao()` 每次执行都会 `new` 一个 DAO，所以直接调用会创建额外对象。依赖改用方法参数传入即可避免这个问题，见 `AppConfig`。
- `AppConfig`、`FullConfig`、`LiteConfig` 分别作为入口加载，`DaoConfig` 由 `AppConfig` 引入；不要一起扫描或注册这些配置，否则相同 Bean 名会发生冲突。手动 `new FullConfig()` 也不会得到 Spring 增强的配置对象。

从 `AppConfig` 开始，再运行 `JavaConfigTest` 的四个示例：`@Import` 引入配置与参数注入、注册库对象，以及方法代理开关的对照。通过 `assertSame` / `assertNotSame` 比较对象身份：

```bash
mvn -pl 01-spring-ioc -Dtest=JavaConfigTest test
```

参考 Spring 官方文档：[@Bean 与 @Configuration](https://docs.spring.io/spring-framework/reference/core/beans/java/basic-concepts.html)、[@Bean 方法参数与命名](https://docs.spring.io/spring-framework/reference/core/beans/java/bean-annotation.html)、[@Import 组合配置](https://docs.spring.io/spring-framework/reference/core/beans/java/composing-configuration-classes.html)。

## 多候选 Bean：lesson05.autowiring

`dao.UserDao` 接口有两个实现类，放在 `dao.impl` 下：`DatabaseUserDao`（模拟数据库）和 `MemoryUserDao`（模拟内存），分别注册为 `databaseUserDao`、`memoryUserDao`。Service 依赖接口，由容器选择具体实现，运行时无需外部服务。

| 示例 | 注入规则 | 查询结果 |
| ---- | -------- | -------- |
| `PrimaryUserService` | 构造器按类型注入，优先选择标有 `@Primary` 的 `databaseUserDao` | `Ada (database)` |
| `QualifierUserService` | 构造器参数使用 `@Qualifier("memoryUserDao")` 筛选候选 | `Ada (memory)` |
| `ResourceUserService` | Setter 使用 `@Resource(name = "memoryUserDao")` 按名称注入 | `Ada (memory)` |

- `@Primary` 标在候选 Bean 上，提供默认选择；它不会删除其他候选。
- `@Qualifier` 标在注入点上，在类型匹配的候选中继续筛选；可使用 Bean 名或限定标签。一般业务必需依赖优先用构造器注入，存在多个候选时搭配 `@Qualifier`。
- `@Resource` 使用 `jakarta.annotation.Resource`，支持字段和单参数 Setter，不支持构造器；显式名称不存在时直接报错。未指定名称时，先找与字段名或 Setter 属性名相同的 Bean，默认名称不存在时再按类型解析。

`AutowiringTest` 保留三个核心示例，分别验证 `@Primary`、`@Qualifier` 和 `@Resource` 的注入结果，每个示例只用一个断言。

```bash
mvn -pl 01-spring-ioc -Dtest=AutowiringTest test
```

参考 Spring 官方文档：[@Primary](https://docs.spring.io/spring-framework/reference/core/beans/annotation-config/autowired-primary.html)、[@Qualifier](https://docs.spring.io/spring-framework/reference/core/beans/annotation-config/autowired-qualifiers.html)、[@Resource](https://docs.spring.io/spring-framework/reference/core/beans/annotation-config/resource.html)。

## Bean 作用域：lesson06.scope

作用域决定容器获取 Bean 时是否复用已有对象。本课直接用 `UserDao` 注册两个 Bean，比较 `singleton`（单例）与 `prototype`（原型）。

| 示例 | 获取方式 | 结果 |
| ---- | -------- | ---- |
| `singletonUserDao` | 反复 `getBean` | 同一实例 |
| `prototypeUserDao` | 反复 `getBean` | 不同实例 |
| `UserService` | 构造器直接接收原型 DAO，反复调用 `getUserDao()` | 同一实例 |
| `ProviderUserService.getSingletonUserDao()` | 通过 `ObjectProvider.getObject()` 获取单例 DAO | 同一实例 |
| `ProviderUserService.getUserDao()` | 通过 `ObjectProvider.getObject()` 获取原型 DAO | 不同实例 |

- **单例是默认作用域**，不写 `@Scope` 等同于 `@Scope("singleton")`。复用范围是同一个容器中的同一个 Bean 定义，不表示整个 JVM 中该类只能创建一个对象。
- **原型用 `@Scope("prototype")` 声明**，每次向容器获取时创建新对象。`@Scope` 可以标在 `@Bean` 方法或组件类上。
- **原型不会自动刷新已有引用**。单例服务创建时直接注入原型 DAO，服务之后一直保存那个对象。需要反复获取新对象时，注入 `ObjectProvider<UserDao>` 并调用 `getObject()`。
- `ObjectProvider` 按目标 Bean 的作用域获取对象，本身不保证每次都是新对象；本例通过两个带 `@Qualifier` 的参数，分别注入获取单例和原型 DAO 的入口。

这也解释了 lesson04 中反复 `getBean` 取得同一对象的原因：默认作用域是单例。

`ScopeTest` 保留四个简单测试，最后一个用两个身份比较断言，对照 `ObjectProvider` 获取单例和原型的结果。初始化和销毁回调留到 lesson07，Web 请求等作用域留到 Web 相关课程。

```bash
mvn -pl 01-spring-ioc -Dtest=ScopeTest test
```

参考 Spring 官方文档：[Bean 作用域](https://docs.spring.io/spring-framework/reference/core/beans/factory-scopes.html)、[ObjectProvider](https://docs.spring.io/spring-framework/docs/7.0.9/javadoc-api/org/springframework/beans/factory/ObjectProvider.html)。
