package com.mdd.mobile.vo.Account;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "Account Detail Vo")
public class AccountDetailVo {

    @ApiModelProperty("Phone Number")
    private String phoneNumber;

    @ApiModelProperty("Phone Number")
    private String Number;

    @ApiModelProperty("email")
    private String nickName;

    @ApiModelProperty("Account type")
    private  Integer currencyType;

    @ApiModelProperty("VerifyAccount")
    private String verifyAccount;

    @ApiModelProperty("Airpay Id")
    private String payId;

    @ApiModelProperty("Incoming amount limit")
    private String incomingAmountLimit;

    @ApiModelProperty("Available incoming amount Limit")
    private String availableIncomingL;

    @ApiModelProperty("Deposit Limit")
    private String depositLimit;

    @ApiModelProperty ("Available deposit Limit")
    private String  availabledepositL;

    @ApiModelProperty ("Withdrawal Limit")
    private String  withdrawalLimit;

    @ApiModelProperty("Available Withdrawal Limit")
    private String  availableWithdrawalL;







}
