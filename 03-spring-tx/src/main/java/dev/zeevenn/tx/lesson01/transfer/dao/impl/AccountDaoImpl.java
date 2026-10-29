package dev.zeevenn.tx.lesson01.transfer.dao.impl;

import java.math.BigDecimal;

import dev.zeevenn.tx.lesson01.transfer.dao.AccountDao;
import org.springframework.jdbc.core.JdbcTemplate;

// TODO：将 DAO 实现注册为组件。
public class AccountDaoImpl implements AccountDao {

    // TODO：定义字段保存 JdbcTemplate。

    public AccountDaoImpl(JdbcTemplate jdbcTemplate) {
        // TODO：保存传入的 JdbcTemplate。
    }

    @Override
    public BigDecimal findBalanceById(int accountId) {
        // TODO：使用 queryForObject 按 id 查询 account 表的 balance，结果类型为 BigDecimal。
        // SQL 参数使用 ? 占位，通过方法参数传入。
        throw new UnsupportedOperationException("TODO：查询账户余额");
    }

    @Override
    public int decreaseBalance(int accountId, BigDecimal amount) {
        // TODO：使用 update 扣减余额，只更新余额足够的账户，返回受影响的行数。
        throw new UnsupportedOperationException("TODO：扣减余额");
    }

    @Override
    public int increaseBalance(int accountId, BigDecimal amount) {
        // TODO：使用 update 增加指定账户的余额，返回受影响的行数。
        throw new UnsupportedOperationException("TODO：增加余额");
    }
}
