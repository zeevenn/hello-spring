# IoC 学习目录

## 学习顺序

| 顺序 | 子包               | 练习内容                                           | 计划测试                                   |
| ---- | ------------------ | -------------------------------------------------- | ------------------------------------------ |
| 01   | `lesson01.basic`            | 手动组装与容器组装、Bean 获取                      | `BasicIocTest`：查询结果、Bean 获取        |
| 02   | `lesson02.xmlconfig`        | 构造器、Setter、普通值、工厂方法                   | `XmlConfigTest`：注入结果、类型转换        |
| 03   | `lesson03.annotationconfig` | 组件标记、扫描、构造器注入                         | `AnnotationConfigTest`：组件注册、缺失依赖 |
| 04   | `lesson04.javaconfig`       | `@Bean`、参数注入、`proxyBeanMethods`              | `JavaConfigTest`：实例身份比较             |
| 05   | `lesson05.autowiring`       | 多候选 Bean、`@Primary`、`@Qualifier`、`@Resource` | `AutowiringTest`：歧义与候选选择           |
| 06   | `lesson06.scope`            | 单例、原型、单例依赖原型                           | `ScopeTest`：实例复用与按需获取            |
| 07   | `lesson07.lifecycle`        | 初始化、销毁、容器关闭                             | `LifecycleTest`：回调顺序与销毁范围        |
| 08   | `lesson08.extension`        | Bean 定义与实例的后处理器                          | `ExtensionTest`：定义修改与实例处理        |

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
