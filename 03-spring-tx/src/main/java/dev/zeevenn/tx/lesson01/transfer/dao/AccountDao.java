package dev.zeevenn.tx.lesson01.transfer.dao;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal findBalanceById(int accountId);

    // 更新方法返回受影响的行数，服务据此判断账户是否存在、扣款是否成功。
    int decreaseBalance(int accountId, BigDecimal amount);

    int increaseBalance(int accountId, BigDecimal amount);
}
