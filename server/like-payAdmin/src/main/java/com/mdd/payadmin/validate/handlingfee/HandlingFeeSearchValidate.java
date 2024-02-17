package com.mdd.payadmin.validate.handlingfee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("Handling Fee Search Validate")
public class HandlingFeeSearchValidate implements Serializable {

    @ApiModelProperty("Transaction Number")
    private String transactionNumber;

    @ApiModelProperty("Pay Id")
    private String payId;

    @ApiModelProperty("Currency Type")
    private Integer currencyType;

    @ApiModelProperty("Transaction Type")
    private Integer typeDetail;
}
