package com.mdd.payadmin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("Handling Fee Vo")
public class HandlingFeeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("Transaction Number")
    private String transactionNumber;

    @ApiModelProperty("Detail Id")
    private String detailId;

    @ApiModelProperty("Currency Name")
    private String currency;

    @ApiModelProperty("Amount")
    private BigDecimal amount;

    @ApiModelProperty("Total Amount")
    private BigDecimal realAmount;

    @ApiModelProperty("Transaction Type")
    private String transactionType;

    @ApiModelProperty("Commission")
    private String commission;

    @ApiModelProperty("Channel Handling Fee")
    private Double channelHandlingFee;

    @ApiModelProperty("Aipay Handling Fee")
    private Double aipayHandlingFee;

}
