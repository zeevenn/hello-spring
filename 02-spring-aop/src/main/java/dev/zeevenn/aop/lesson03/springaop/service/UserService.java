package dev.zeevenn.aop.lesson03.springaop.service;

import org.springframework.stereotype.Service;

/**
 * 目标对象（Target object）：被切面增强的真实业务对象，负责最终的查询操作。
 * findNameById(int id) 的执行是本例的连接点；是否为它添加通知，由切入点表达式决定。
 */
@Service
public class UserService {

    public String findNameById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("查询用户 id 必须大于 0");
        }
        return "Ada" + id;
    }
}
