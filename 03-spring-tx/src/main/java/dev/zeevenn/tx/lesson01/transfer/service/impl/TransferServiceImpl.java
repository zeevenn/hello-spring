package dev.zeevenn.tx.lesson01.transfer.service.impl;

import java.math.BigDecimal;

import dev.zeevenn.tx.lesson01.transfer.dao.AccountDao;
import dev.zeevenn.tx.lesson01.transfer.service.TransferService;

// TODO：将服务实现注册为组件。
public class TransferServiceImpl implements TransferService {

    // TODO：定义字段保存 AccountDao。

    public TransferServiceImpl(AccountDao accountDao) {
        // TODO：保存传入的 AccountDao。
    }

    // TODO：在实现方法上添加 Spring 的 @Transactional，让扣款和入款属于同一个事务。
    @Override
    public void transfer(int fromAccountId, int toAccountId, BigDecimal amount) {
        // TODO：校验金额大于 0，转出和转入账户不同。
        // TODO：扣减转出账户余额，影响行数不是 1 时抛出运行时异常。
        // TODO：增加转入账户余额，影响行数不是 1 时抛出运行时异常。
        // 异常要传到事务代理，代理才能按回滚规则回滚之前的更新。
    }
}
