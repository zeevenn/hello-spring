package dev.zeevenn.tx.lesson01.transfer;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TransferApp {

    public static void main(String[] args) {
        // 创建 AnnotationConfigApplicationContext，从容器获取 AccountDao 和 TransferService。
        // TODO：打印账户 1、2 的余额，调用 transfer(1, 2, new BigDecimal("100.00"))，再查询余额。
        // TODO：执行 reset.sql 恢复初始余额后，再向不存在的账户 999 转账，观察回滚后的余额。
        // MySQL 数据会持久保存，重新创建 Spring 容器不会重置账户余额。
        // 在调用方捕获异常后再查询余额，确认转出账户之前的扣款也已撤销。

        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {

        }
    }
}
