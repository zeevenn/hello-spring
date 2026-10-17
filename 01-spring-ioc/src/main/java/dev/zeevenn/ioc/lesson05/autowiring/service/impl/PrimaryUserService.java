package dev.zeevenn.ioc.lesson05.autowiring.service.impl;

import dev.zeevenn.ioc.lesson05.autowiring.dao.UserDao;
import org.springframework.stereotype.Service;

/**
 * 优先使用构造器注入，明确表达业务运行必需的依赖，并支持 final 字段。
 * 此处未指定限定符，两个 UserDao 候选中的 @Primary 决定默认选择。
 */
@Service
public class PrimaryUserService {

    private final UserDao userDao;

    // 单构造器无需 @Autowired；参数名不匹配任何 Bean 名，突出 @Primary 的作用。
    public PrimaryUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public String findName() {
        return this.userDao.findName();
    }
}
