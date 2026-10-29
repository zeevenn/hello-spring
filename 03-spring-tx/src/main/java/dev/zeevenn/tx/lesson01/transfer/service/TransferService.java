package dev.zeevenn.tx.lesson01.transfer.service;

import java.math.BigDecimal;

public interface TransferService {

    void transfer(int fromAccountId, int toAccountId, BigDecimal amount);
}
