package dev.zeevenn.ioc.lesson03.annotationconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 字段注入：Spring 创建对象后，直接为标注 @Autowired 的字段注入依赖。
 * 一般业务代码优先使用构造器注入，让必需依赖更明确。
 * 字段注入的依赖不能声明为 final，直接 new FieldUserService() 时 userDao 仍为 null，
 * 脱离容器做单元测试时，需要通过反射或测试框架为私有字段赋值。
 */
@Service
public class FieldUserService {

    @Autowired
    private UserDao userDao;

    public String findName() {
        return this.userDao.findName();
    }
}
