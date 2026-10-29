# Spring 事务学习目录

## 第一课：转账与声明式事务

`lesson01.transfer` 连接本地 MySQL，使用两个账户演示：一次转账包含扣款和入款，两次更新应当一起提交，或者一起回滚。

当前保留 Maven 模块、依赖、连接配置模板，以及 Java 类、方法声明和 SQL 文件。注解、SQL 和 Java 实现按 TODO 自行完成；非 `void` 方法暂用 `UnsupportedOperationException` 占位，保证骨架可以编译，实现后删除占位语句。

| 文件 | 需要完成的内容 |
| ---- | -------------- |
| `AppConfig` | 数据源、`JdbcTemplate`、事务管理器，以及启用注解事务 |
| `dao/AccountDao` | 已声明查询余额、扣款和入款接口 |
| `dao/impl/AccountDaoImpl` | 构造函数注入 `JdbcTemplate`，编写查询和更新 SQL |
| `service/TransferService` | 已声明转账接口 |
| `service/impl/TransferServiceImpl` | 注入 DAO，完成转账流程并添加 `@Transactional` |
| `TransferApp` | 从容器调用业务，打印成功提交和失败回滚前后的余额 |
| `resources/lesson01/transfer/database.properties` | JDBC 驱动、连接 URL 和账号配置 |
| `resources/lesson01/transfer/schema.sql` | 编写建库和建表 SQL |
| `resources/lesson01/transfer/data.sql` | 编写初始化 SQL，两个账户各有 1000.00 元 |
| `resources/lesson01/transfer/reset.sql` | 编写重置余额 SQL，便于重复练习 |

## 本地数据库准备

使用 MySQL 8.0 或以上版本。完成 SQL 文件中的 TODO，再在 IDEA 数据库窗口或 MySQL 客户端依次执行：

1. `schema.sql`：创建 `hello_spring_tx` 数据库和 InnoDB 账户表。
2. `data.sql`：首次插入两个练习账户。

InnoDB 支持事务，扣款和入款在同一个事务中完成。MySQL 数据持久保存，重启程序不会重置；重复练习前执行 `reset.sql`，将两个账户恢复到 1000.00 元。

连接配置位于 `src/main/resources/lesson01/transfer/database.properties`。默认 URL 为 `jdbc:mysql://localhost:3306/hello_spring_tx`，用户名为 `root`。在 IDEA 的运行配置中设置环境变量：

```text
MYSQL_USER=你的数据库用户名
MYSQL_PASSWORD=你的数据库密码
```

如果地址、端口或数据库名称不同，设置 `MYSQL_URL` 为对应的 JDBC URL；数据库名变化时，也要同步调整 SQL 脚本中的库名。真实密码通过运行配置传入，配置文件保留环境变量占位符。

## 实现顺序

1. 编写建库、建表、初始化和重置余额 SQL，完成数据库准备，再填写连接配置。
2. 完成 `AppConfig` 的数据源和 `JdbcTemplate`，连接本地 MySQL。
3. 完成 DAO，先确认能查询和更新余额。
4. 完成转账流程，检查每次更新的影响行数，失败时抛出运行时异常。
5. 使用 `DataSourceTransactionManager` 配置事务管理器，并添加 `@EnableTransactionManagement`。
6. 给转账实现方法添加 `org.springframework.transaction.annotation.Transactional`。
7. 完成 `TransferApp`，观察两种结果。

`mysql-connector-j` 是 JDBC 驱动，`DataSource` 提供连接，`JdbcTemplate` 封装 JDBC 的连接、预编译语句和结果集处理。DAO 中继续编写 SQL，并使用 `?` 绑定参数。

金额使用 `BigDecimal`，表字段使用 `DECIMAL`。创建金额时使用字符串，例如 `new BigDecimal("100.00")`。

| 场景 | 初始余额 | 操作 | 预期余额 |
| ---- | -------- | ---- | -------- |
| 成功提交 | 账户 1、2 各 1000.00 | 账户 1 向账户 2 转账 100.00 | 账户 1 为 900.00，账户 2 为 1100.00 |
| 失败回滚 | 执行 `reset.sql` 后，两账户各 1000.00 | 账户 1 向不存在的账户 999 转账 100.00，入款失败并抛出异常 | 两账户仍各为 1000.00 |

实现完成后在 IDEA 中运行 `TransferApp.main()`。转账通过容器中的服务代理调用，异常传出业务方法后，事务代理才会根据回滚规则处理；调用方可以捕获异常，再查询回滚后的余额。

## 依赖与检查

- `spring-context`：从父 POM 继承。
- `spring-jdbc`：提供 `JdbcTemplate` 和 JDBC 事务管理器，同时传递引入 `spring-tx`。
- `mysql-connector-j`：MySQL JDBC 驱动，运行时使用。

检查骨架能否编译：

```bash
mvn -pl 03-spring-tx compile
```

当前不添加测试。数据源等方法仍是占位实现，开始编写后再运行容器。

参考官方文档：[MySQL JDBC 驱动](https://dev.mysql.com/doc/connector-j/en/connector-j-installing-maven.html)、[驱动版本与数据库兼容性](https://dev.mysql.com/doc/relnotes/connector-j/en/news-9-7-0.html)、[数据源与 JDBC 连接](https://docs.spring.io/spring-framework/reference/data-access/jdbc/connections.html)、[声明式事务](https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative/annotations.html)。
