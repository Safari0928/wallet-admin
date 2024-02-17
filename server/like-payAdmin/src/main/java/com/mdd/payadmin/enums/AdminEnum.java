package com.mdd.payadmin.enums;

public enum AdminEnum {
    SUCCESS(200, "Success"),
    FAILED(300, "Failed"),
    PENDING_NOT_APPROVAL(400,"This transaction cannot be verified"),
    REQUEST_404_ERROR(404, "The request interface does not exist"),
    SYSTEM_ERROR(500, "System error"),
    REDIS_CONNECTION_ERROR(600, "Failed to connect with Redis"),
    LOGIN_WRONG(1002,"Wrong user name or Password"),
    ID_DISABLE(1006,"This account is Disable!"),
    NO_USER( 1112,"No such user"),
    WRONG_PASSWORD(330,"Wrong password"),
    UPDATE_ERROR(1014,"Update is failed");
    private final int code;
    private final String msg;

    AdminEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 获取状态码
     *
     * @author fzr
     * @return Long
     */
    public int getCode() {
        return this.code;
    }

    /**
     * 获取提示
     *
     * @author fzr
     * @return String
     */
    public String getMsg() {
        return this.msg;
    }
}
