package com.mdd.payadmin.enums;

public enum GraphEnums {

    REVENUE(1,"revenue","getRevenue"),

    DEPOSIT(2,"deposit","getDeposit"),

    WITHDRAWAL(3,"withdrawal","getWithdrawal"),

    TRANSFER(4,"transfer","getTransfer"),

    EXCHANGES(5,"exchanges","getExchanges"),

    USERS(6,"users","getUsers");

    private final int code;
    private final String name;
    private final String msg;

    GraphEnums(int code,String name, String msg){
        this.code = code;
        this.name=name;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public String getMsg() {
        return this.msg;
    }

}
