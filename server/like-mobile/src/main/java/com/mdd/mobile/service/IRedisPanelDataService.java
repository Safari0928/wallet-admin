package com.mdd.mobile.service;

import java.math.BigDecimal;

public interface IRedisPanelDataService {
    boolean addDepositAmount( BigDecimal amount);

    boolean addUserCount();

    boolean addTransferAmount( BigDecimal amount);

    boolean addWithdrawAmount( BigDecimal amount);

    boolean addTransactionAmount( BigDecimal amount);

    boolean addTransactionRevenue( BigDecimal amount);

    boolean addOrdersCount();
}
