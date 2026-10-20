package dev.zeevenn.ioc.lesson05.autowiring.service.impl;

import dev.zeevenn.ioc.lesson05.autowiring.dao.UserDao;
import dev.zeevenn.ioc.lesson05.autowiring.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * {@code @Resource} 是 Jakarta 标准注解，无需搭配 @Autowired。
 * 显式指定 name 时按 Bean 名查找，找到的 Bean 仍必须能赋给注入点的类型；
 * 名称不存在时直接报错，不会因为另一个 Bean 标了 @Primary 就改用它。
 * 不指定 name 时，默认使用字段名或 Setter 属性名；默认名称不存在时，
 * Spring 默认再尝试按类型解析，此时可能使用 @Primary。
 *
 * {@code @Resource} 支持字段或单参数 Setter，不支持构造器注入。
 * 此处用 Setter 演示按名称注入；一般业务的必需依赖仍优先考虑构造器 + @Qualifier。
 */
@Service
public class ResourceUserService implements UserService {

    private UserDao userDao;

    @Resource(name = "memoryUserDao")
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public String findName() {
        return this.userDao.findName();
    }
}
