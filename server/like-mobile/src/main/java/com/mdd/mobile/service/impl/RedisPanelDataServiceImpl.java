package com.mdd.mobile.service.impl;

import com.mdd.common.util.RedisUtils;
import com.mdd.mobile.enums.TypeDetailEnums;
import com.mdd.mobile.service.IRedisPanelDataService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RedisPanelDataServiceImpl implements IRedisPanelDataService {

    final String key ="PanelData: ";
    @Override
    public boolean addDepositAmount(BigDecimal amount) {
        String depositKey =key+ TypeDetailEnums.TYPE_DEPOSIT.getMsg() +": "+ TypeDetailEnums.TYPE_DEPOSIT.getCode()+TypeDetailEnums.TYPE_DEPOSIT.getMsg();
        if (!RedisUtils.exists(depositKey)){
            RedisUtils.set(depositKey,amount);
        }else {
            BigDecimal oldAmount = (BigDecimal) RedisUtils.get(depositKey);
            RedisUtils.set(depositKey,oldAmount.add(amount));
        }
        return true;
    }

    @Override
    public boolean addUserCount() {
        String userKey =key+TypeDetailEnums.TYPE_USER.getMsg()+": "+TypeDetailEnums.TYPE_USER.getCode()+TypeDetailEnums.TYPE_USER.getMsg();
        if (!RedisUtils.exists(userKey)){
            RedisUtils.set(userKey,0);
        }
        else {
            int oldCount = (int) RedisUtils.get(userKey);
            RedisUtils.set(userKey,oldCount+1);
        }
        return true;
    }

    @Override
    public boolean addTransferAmount(BigDecimal amount) {
        String transferKey =key+TypeDetailEnums.TYPE_TRANSFER_DEPOSIT.getMsg()+": "+TypeDetailEnums.TYPE_TRANSFER_DEPOSIT.getCode()+TypeDetailEnums.TYPE_TRANSFER_DEPOSIT.getMsg();
        if (!RedisUtils.exists(transferKey)){
            RedisUtils.set(transferKey,amount);
        }
        else {
            BigDecimal oldAmount = (BigDecimal) RedisUtils.get(transferKey);
            RedisUtils.set(transferKey,oldAmount.add(amount));
        }
        return true;
    }

    @Override
    public boolean addWithdrawAmount(BigDecimal amount) {
        String transferKey =key+TypeDetailEnums.TYPE_WITHDRAWAL.getMsg()+": "+TypeDetailEnums.TYPE_WITHDRAWAL.getCode()+TypeDetailEnums.TYPE_WITHDRAWAL.getMsg();
        if (!RedisUtils.exists(transferKey)){
            RedisUtils.set(transferKey,amount);
        }
        else {
            BigDecimal oldAmount = (BigDecimal) RedisUtils.get(transferKey);
            RedisUtils.set(transferKey,oldAmount.add(amount));
        }
        return true;
    }

    @Override
    public boolean addTransactionAmount(BigDecimal amount) {
        String TransactionKey =key+TypeDetailEnums.TYPE_TRANSACTION.getMsg()+": "+TypeDetailEnums.TYPE_TRANSACTION.getCode()+TypeDetailEnums.TYPE_TRANSACTION.getMsg();
        if (!RedisUtils.exists(TransactionKey)){
            RedisUtils.set(TransactionKey,amount);
        }else {
            BigDecimal oldAmount = (BigDecimal) RedisUtils.get(TransactionKey);
            RedisUtils.set(TransactionKey,oldAmount.add(amount));
        }
        return true;
    }

    @Override
    public boolean addTransactionRevenue(BigDecimal amount) {
        String TransactionRevenueKey =key+TypeDetailEnums.TYPE_REVENUE.getMsg()+": "+TypeDetailEnums.TYPE_REVENUE.getCode()+TypeDetailEnums.TYPE_REVENUE.getMsg();
        if (!RedisUtils.exists(TransactionRevenueKey)){
            RedisUtils.set(TransactionRevenueKey,amount);
        }else {
            BigDecimal oldAmount = (BigDecimal) RedisUtils.get(TransactionRevenueKey);
            RedisUtils.set(TransactionRevenueKey,oldAmount.add(amount));
        }
        return true;
    }

    @Override
    public boolean addOrdersCount() {
        String orderKey =key+TypeDetailEnums.TYPE_ORDER_COUNT.getMsg()+": "+TypeDetailEnums.TYPE_ORDER_COUNT.getCode()+TypeDetailEnums.TYPE_ORDER_COUNT.getMsg();
        if (!RedisUtils.exists(orderKey)){
            RedisUtils.set(orderKey,0);
        }else {
            int oldAmount = (int) RedisUtils.get(orderKey);
            RedisUtils.set(orderKey,oldAmount+1);
        }
        return true;
    }
}
