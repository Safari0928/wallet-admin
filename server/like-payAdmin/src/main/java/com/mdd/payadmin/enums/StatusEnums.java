package com.mdd.payadmin.enums;

public enum StatusEnums {
    SUCCESS(1,"Success"),
    FAIL(-1,"Failed"),
    PENDING(2,"Pending"),
    NOT_ACTIVE(0,"Not Active");


    private final int code;
    private final String msg;

    StatusEnums(int code, String msg) {
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
