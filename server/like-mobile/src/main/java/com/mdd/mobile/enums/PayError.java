package com.mdd.mobile.enums;

public enum PayError {
    SUCCESS(200, "Success"),
    FAILED(300, "Failed"),
    REQUEST_404_ERROR(404, "The request interface does not exist"),
    SYSTEM_ERROR(500, "System error"),
    CAPTCHA_ERROR(1001, "Incorrect verification code"),
    CODE_NOT_FOUND(1002, "Verification code not found"),
    CODE_NOT_MATCHED(1003,"Codes do not match"),
    CODE_COULDNT_SEND(1004,"Code could not be sent"),
    USER_NOT_FOUND(1010,"User Not found"),
    LOGIN_ACCOUNT_ERROR(1012, "Wrong phone number or password"),
    PAYMENT_ERROR(1013,"insufficient balance or limit"),
    LOGIN_DISABLE_ERROR(1021, "The account has been disabled."),
    USER_ACCOUNT_NOT_VERIFIED(1022,"Account not verified"),
    BANKCARD_ALREADY_EXIST(1031,"This Bankcard already exists!"),
    BANKCARD_NOT_FOUND(1032,"Bank card not found"),
    DEPOSIT_NOT_FOUND(1033 ,"Deposit not found try again!"),
    UPDATE_AMOUNT_FILED(1034,"Update amount filed"),
    ASSERT_REDIS_ERROR(1035, "Asserting Redis Errors"),
    ASSERT_MYBATIS_ERROR(1036, "Asserting Mybatis Errors"),
    PROCESS_NOT_FINISHED(1037,"Transaction still in progress. Please enter verify code"),
    BANK_ACCOUNT_ALREADY_EXIST(1040,"This bank account already exists!"),
    BANK_ACCOUNT_ALREADY_DELETE(1041,"This bank account already deleted"),
    REGISTERED_USER_ERROR(1065,"User already registered"),
    PARAMS_VALID_ERROR(1111, "Amount can not be '0' or invalid pay ID"),
    PARAM_ERROR(1112,"Wrong Parameter"),
    PASSWORD_ERROR(1113,"The old password and the entered password are not the same"),
    PASSWORD_NOT_MATCHED(1114,"The new passwords are not the same"),
    MAIL_NOT_MATCHED(1115,"Your old e-mail is not correct"),
    CONFIRMATION_CODE_REQUIRED(1120,"Please enter the confirmation code received on your phone");
    /**
     * 构造方法
     */
    private final int code;
    private final String msg;
    PayError(int code, String msg) {
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
