package com.mdd.payadmin.validate.deposit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
@ApiModel("Deposit Search Validate")
public class DepositSearchValidate implements Serializable {

    @ApiModelProperty("Transact Number")
    private String transactionNumber;

    @ApiModelProperty("Cart Number")
    private String cardNumber;

    @ApiModelProperty("PayId")
    private String payId;

    @ApiModelProperty("Deposit Status:[-1:Fail 1:Success]")
    private Integer status;
}
