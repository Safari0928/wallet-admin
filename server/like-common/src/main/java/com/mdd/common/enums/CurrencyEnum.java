package com.mdd.common.enums;

public enum CurrencyEnum {

    TRY(1,"TRY"),
    USD(2,"USD"),
    EUR(3,"EUR");

    private final int code;
    private final String msg;

    CurrencyEnum(int code, String msg) {
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
