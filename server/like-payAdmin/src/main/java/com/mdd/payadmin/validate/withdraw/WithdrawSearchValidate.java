package com.mdd.payadmin.validate.withdraw;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("Withdraw Search Validate")
public class WithdrawSearchValidate implements Serializable {

    @ApiModelProperty("Transaction Number")
    private String transactionNumber;

    @ApiModelProperty("Pay Id")
    private String payId;

    @ApiModelProperty("Iban")
    private String iban;

    @ApiModelProperty("Status")
    private Integer status;
}
