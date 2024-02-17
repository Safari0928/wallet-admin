package com.mdd.mobile.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UserInfoVo implements Serializable {

    @ApiModelProperty("Nick Name")
    private String nickName;

    @ApiModelProperty("Phone Number")
    private String phoneNumber;

    @ApiModelProperty("Email")
    private String email;

    @ApiModelProperty(value="Verify Account ", notes="-1=Unverified 1=Verified 2=Pending  Default=-1")
    private Integer verifyAccount;

    @ApiModelProperty("Airpay id")
    private String payId;

    @ApiModelProperty("Transfer Limit")
    private BigDecimal transferLimit;

    @ApiModelProperty("Transfer Limit")
    private BigDecimal usedTransferLimit;

    @ApiModelProperty("Deposit Limit")
    private BigDecimal depositLimit;

    @ApiModelProperty("Transfer Limit")
    private BigDecimal usedDepositLimit;

    @ApiModelProperty("Withdrawal Limit")
    private BigDecimal withdrawalLimit;

    @ApiModelProperty("Withdrawal  Limit")
    private BigDecimal usedWithdrawalLimit;

}
