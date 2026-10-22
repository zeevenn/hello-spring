package dev.zeevenn.aop.lesson01.staticproxy;

/**
 * 真实业务对象：只负责查询用户名称。
 */
public class UserServiceImpl implements UserService {

    @Override
    public String findNameById(int id) {
        return "Ada" + id;
    }
}
