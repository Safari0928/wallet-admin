package com.mdd.payadmin.vo.deposit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("Deposit list Vo")
public class DepositListVo implements Serializable {

    @ApiModelProperty("Deposit Id")
    private String depositId;

    @ApiModelProperty("Transact Number")
    private String transactionNumber;

    @ApiModelProperty("Cart Number")
    private String cardNumber;

    @ApiModelProperty("Bank Name ")
    private String bankName;

    @ApiModelProperty("fullName ")
    private String  fullName;

    @ApiModelProperty("Amount")
    private BigDecimal amount;

    @ApiModelProperty("Real Amount")
    private BigDecimal realAmount;

    @ApiModelProperty("PayId")
    private String payId;

    @ApiModelProperty("Deposit Type:[1:Deposit]")
    private Integer typeDetail;

    @ApiModelProperty("Deposit message:[1:Deposit]")
    private String typeMSG;

    @ApiModelProperty("Deposit Status:[-1:Fail 1:Success]")
    private Integer status;

    @ApiModelProperty("Deposit Status:[-1:Fail 1:Success]]")
    private String statusMSG;

    @ApiModelProperty("创建时间,Create Time")
    private String  createTime;

    @ApiModelProperty("删除时间,Complete Time")
    private String  completeTime;
}
