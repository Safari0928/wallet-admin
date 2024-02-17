package com.mdd.payadmin.vo.deposit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("Deposit detail Vo")
public class DepositDetailVo implements Serializable {

    @ApiModelProperty("PayId")
    private String payId;

    @ApiModelProperty("Deposit Id")
    private String depositId;

    @ApiModelProperty("Transact Number")
    private String transactionNumber;

    @ApiModelProperty("Cart Number")
    private String cardNumber;

    @ApiModelProperty("Expiry date")
    private String expiryDate;

    @ApiModelProperty("CVV")
    private String cvv;

    @ApiModelProperty("Phone_number")
    private String phoneNumber;

    @ApiModelProperty("Transact Number")
    private String bankName;

    @ApiModelProperty("Transact Number")
    private String fullName;

    @ApiModelProperty("Cart Number")
    private BigDecimal amount;

    @ApiModelProperty("Transact Number")
    private BigDecimal realAmount;

    @ApiModelProperty("Commision")
    private Double commission ;

    @ApiModelProperty("Commision")
    private Double payCommission ;

    @ApiModelProperty("Channel Commission")
    private Double channelCommission;

    @ApiModelProperty("Deposit Type:[1:Deposit]")
    private Integer typeDetail;

    @ApiModelProperty("Deposit message:[1:Deposit]")
    private String typeMSG;

    @ApiModelProperty("Deposit Status:[-1:Fail 1:Success]")
    private Integer status;

    @ApiModelProperty("Deposit Status:[-1:Fail 1:Success]]")
    private String statusMSG;

    @ApiModelProperty("创建时间,Create Time")
    private String createTime;

    @ApiModelProperty("更新时间,Update Time")
    private String completeTime;

}
