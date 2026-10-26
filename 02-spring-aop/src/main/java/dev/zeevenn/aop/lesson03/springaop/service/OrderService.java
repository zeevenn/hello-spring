package dev.zeevenn.aop.lesson03.springaop.service;

import org.springframework.stereotype.Service;

/**
 * 第二个业务对象，用于观察同一条切入点规则如何覆盖不同的类和返回值类型。
 */
@Service
public class OrderService {

    public int countOrders() {
        return 2;
    }
}
