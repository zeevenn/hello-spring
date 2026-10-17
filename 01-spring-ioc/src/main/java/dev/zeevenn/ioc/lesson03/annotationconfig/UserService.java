package dev.zeevenn.ioc.lesson03.annotationconfig;

import org.springframework.stereotype.Service;

/**
 * 构造器注入：一般情况下优先使用，尤其适合业务运行必需的依赖。
 * 必需依赖直接体现在构造器参数中，Spring 创建对象时就会提供这些依赖；
 * 字段可以声明为 final，避免依赖引用在创建后被重新赋值。
 * 单元测试中也可以直接 new UserService(userDao)，无需启动 Spring 容器即可传入测试替身。
 */
@Service
public class UserService {

    private final UserDao userDao;

    // 只有一个构造器时，Spring 自动按参数类型注入，无需 @Autowired。
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public String findName() {
        return this.userDao.findName();
    }
}
