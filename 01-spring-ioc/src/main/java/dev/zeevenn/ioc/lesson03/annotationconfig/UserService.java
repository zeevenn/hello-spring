package dev.zeevenn.ioc.lesson03.annotationconfig;

import org.springframework.stereotype.Service;

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
