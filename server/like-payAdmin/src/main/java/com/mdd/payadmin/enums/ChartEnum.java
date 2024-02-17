package com.mdd.payadmin.enums;

public enum ChartEnum {

    ALL_USERS(1,"All Users"),
    UNVERIFIED(2,"Unverified"),
    VERIFIED(3,"Verified");

    private final int code;
    private final String msg;

    ChartEnum(int code, String msg){
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
