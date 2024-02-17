package com.mdd.common.enums;

public enum ActiveEnums {

    ACTIVE(1,"Active limits"),
    NOT_ACTIVE(-1,"UnActive limits");

    private final int code;
    private final String msg;

    ActiveEnums(int code, String msg) {
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
