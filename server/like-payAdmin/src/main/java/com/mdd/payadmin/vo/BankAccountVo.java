package com.mdd.payadmin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "Bank Account Vo")
public class BankAccountVo implements Serializable {

    @ApiModelProperty("Iban")
    private String  iban;

    @ApiModelProperty("Country")
    private String country;

    @ApiModelProperty("Bank Card Name")
    private String bankCardName;

    @ApiModelProperty("Bank Name")
    private String bankName;

    @ApiModelProperty("Card Type")
    private String cardType;

    @ApiModelProperty(value = "Create Time")
    private String createTime;
}
