package dev.zeevenn.ioc.lesson05.autowiring.dao.impl;

import dev.zeevenn.ioc.lesson05.autowiring.dao.UserDao;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 * {@code @Primary} 标在候选 Bean 上：按类型注入单个 UserDao 时，优先选择此实现。
 * 同一组候选中应只有一个首选 Bean；它不会阻止其他 Bean 注册或被显式选择。
 */
@Repository("databaseUserDao")
@Primary
public class DatabaseUserDao implements UserDao {

    @Override
    public String findName() {
        return "Ada (database)";
    }
}
