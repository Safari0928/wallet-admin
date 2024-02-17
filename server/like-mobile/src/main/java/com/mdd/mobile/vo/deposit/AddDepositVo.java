package com.mdd.mobile.vo.deposit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
@ApiModel(value = "Add Deposit Vo")
public class AddDepositVo implements Serializable {

    @ApiModelProperty("Deposit Id")
    private String depositId;

    @ApiModelProperty(value = "Card Number")
    private String cardNumber;

    @ApiModelProperty(value = "Expiry Date")
    private String expiryDate;

    @ApiModelProperty(value = "CVV Number")
    private String cvv;

    @ApiModelProperty(value = "Full Name on Card")
    private String fullName;

    @ApiModelProperty("Commision")
    private Double commission ;

    @ApiModelProperty("Real Amount")
    private BigDecimal realAmount;

    @ApiModelProperty("Cart Number")
    private BigDecimal amount;

    @ApiModelProperty("Deposit Status:[-1:Fail 1:Success]")
    private Integer status;

}
