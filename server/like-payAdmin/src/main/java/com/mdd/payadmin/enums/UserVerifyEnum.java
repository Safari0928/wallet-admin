package com.mdd.payadmin.enums;

public enum UserVerifyEnum {
    VERIFIED(1,"Verified"),
    UNVERIFIED(-1,"Unverified"),
    PENDING(2,"Pending");

    private final int code;
    private final String msg;


    UserVerifyEnum(int code, String msg) {
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
