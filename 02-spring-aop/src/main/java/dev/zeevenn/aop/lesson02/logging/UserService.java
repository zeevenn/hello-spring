package dev.zeevenn.aop.lesson02.logging;

import org.springframework.stereotype.Service;

/**
 * 目标对象：只处理业务，日志由独立的切面完成。
 */
@Service
public class UserService {

    public String findName() {
        return "Ada";
    }
}
