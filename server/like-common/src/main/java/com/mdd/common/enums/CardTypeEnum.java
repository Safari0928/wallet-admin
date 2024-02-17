package com.mdd.common.enums;
public enum CardTypeEnum {
    VISA(1,"Visa"),

    MASTERCARD(2,"MasterCard"),

    AMERICAN_EXPRESS(3,"American Express"),

    DISCOVER(4,"Discover");

    private final int code;
    private final String msg;

    CardTypeEnum(int code, String msg) {
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
