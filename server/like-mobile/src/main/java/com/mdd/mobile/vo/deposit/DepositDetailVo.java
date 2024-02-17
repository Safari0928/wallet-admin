package com.mdd.mobile.vo.deposit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "Deposit Detaile Vo")
public class DepositDetailVo implements Serializable {

    @ApiModelProperty("Bank Name")
    private String bankName;

    @ApiModelProperty("Receiver NickName")
    private String cardNumber;

    @ApiModelProperty("Transaction Number")
    private String transactionNumber;

    @ApiModelProperty("Deposit Amount")
    private BigDecimal amount;

    @ApiModelProperty("Commission")
    private Double commission;

    @ApiModelProperty("Deposit Status:[0:Fail 1:Success]")
    private Integer status;

    @ApiModelProperty("Deposit Status:[-1:Fail 1:Success]]")
    private String statusMSG;

    @ApiModelProperty("Transaction Type")
    private Integer  TransactionType;

    @ApiModelProperty("Deposit message:[1:Deposit]")
    private String typeMSG;

    @ApiModelProperty("Create Time")
    private String  createTime;

    @ApiModelProperty("Complete Time")
    private String  completeTime;

}
