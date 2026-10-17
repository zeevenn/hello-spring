package dev.zeevenn.ioc.lesson03.annotationconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Setter 注入：Spring 创建对象后，调用标注 @Autowired 的 Setter 方法注入依赖。
 * 一般用于有合理默认行为的可选依赖，或确实需要在对象创建后重新设置依赖的场景；
 * 像本例的 userDao 这样业务运行必需的依赖，通常优先使用构造器注入。
 * 注意：Setter 并不代表依赖自动变成可选，@Autowired 默认仍要求依赖存在。
 * 若使用 @Autowired(required = false)，必须自行保证未注入时也有合理行为。
 */
@Service
public class SetterUserService {

    private UserDao userDao;

    // 注解扫描方式需要 @Autowired；仅声明 Setter 不会自动注入。
    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public String findName() {
        return this.userDao.findName();
    }
}
