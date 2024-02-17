package com.mdd.common.enums;

public enum StatusEnums {
    SUCCESS(1,"Transaction Success"),
    FAIL(-1,"Transaction Fail"),
    PENDING(2,"Transaction Pending"),
    DELETE(-1,"Delete"),
    NOT_DELETE(1,"Not Delete");


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
