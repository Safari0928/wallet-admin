package com.mdd.payadmin.enums;

public enum UserStatusEnum {
    NORMAL(1,"Enable"),
    DISABLED(-1,"Disabled");

    private final int code;
    private final String msg;

    UserStatusEnum(int code, String msg) {
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
