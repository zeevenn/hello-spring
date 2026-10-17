package dev.zeevenn.ioc.lesson05.autowiring.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import dev.zeevenn.ioc.lesson05.autowiring.dao.UserDao;

/**
 * {@code @Qualifier} 标在注入点上：先按 UserDao 类型找候选，再按限定值缩小范围。
 * 本例使用 Bean 名作为限定值，选中 memoryUserDao，排除标有 @Primary 的数据库实现。
 * 限定值也可以匹配 Bean 上声明的 @Qualifier 标签，不局限于 Bean 名。
 */
@Service
public class QualifierUserService {

    private final UserDao userDao;

    // @Qualifier 本身不触发注入；此处由 Spring 的单构造器注入机制提供依赖。
    public QualifierUserService(@Qualifier("memoryUserDao") UserDao userDao) {
        this.userDao = userDao;
    }

    public String findName() {
        return this.userDao.findName();
    }
}
