package com.mdd.mobile.enums;

public enum TypeDetailEnums {
    TYPE_DEPOSIT(1,"Deposit"),
    TYPE_WITHDRAWAL(2,"Withdrawal"),
    TYPE_TRANSFER_DEPOSIT(3,"Transfer Deposit"),
    TYPE_TRANSFER_WITHDRAWAL(4,"Transfer Withdrawal"),
    TYPE_EXCHANGE(5,"Exchange"),
    TYPE_TRANSACTION(6,"Transaction"),
    TYPE_REVENUE(7,"Revenue"),
    TYPE_ORDER_COUNT(8,"Count"),
    TYPE_USER(9,"User Count");


    private final int code;
    private final String msg;

    TypeDetailEnums(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

}
