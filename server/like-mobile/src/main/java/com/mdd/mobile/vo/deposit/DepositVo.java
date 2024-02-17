package com.mdd.mobile.vo.deposit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "Deposit Vo")
public class DepositVo implements Serializable {

    @ApiModelProperty("Deposit Amount")
    private String  depositId;

    @ApiModelProperty("Deposit Amount")
    private BigDecimal amount;

    @ApiModelProperty("Cart Number")
    private String cardNumber;

    @ApiModelProperty("Bank Name ")
    private String bankName;

    @ApiModelProperty("Transfer Type:[3:Transfer-Deposit 4:Transfer-Withdraw]")
    private Integer typeDetail;

    @ApiModelProperty(value = "Create Time")
    private String createTime;


}
