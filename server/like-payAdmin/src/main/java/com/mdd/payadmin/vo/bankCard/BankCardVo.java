package com.mdd.payadmin.vo.bankCard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "Bank Card Vo")
public class BankCardVo implements Serializable {

    @ApiModelProperty("Bank Name ")
    private String  cardNumber;

    @ApiModelProperty(value = "Expiry Date")
    private String expiryDate;

    @ApiModelProperty(value = "CVV Number")
    private String cvv;

    @ApiModelProperty("fullName ")
    private String  fullName;

    @ApiModelProperty("fullName ")
    private String  cardType;

    @ApiModelProperty("Bank Name ")
    private String  bankName;

    @ApiModelProperty(value = "Create Time")
    private String createTime;

}
