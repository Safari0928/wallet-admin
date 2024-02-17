package com.mdd.payadmin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class TransferDetailVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("Transaction Number")
    private String transactionNumber;

    @ApiModelProperty("Pay ID")
    private String payId;

    @ApiModelProperty("Pay ID")
    private String toPayId;

    @ApiModelProperty("Transfer Status:[-1:Fail 1:Success]]")
    private Integer status;

    @ApiModelProperty("Transfer Status message")
    private String statusMSG;

    @ApiModelProperty("Transfer Type:[3:Transfer-Deposit 4:Transfer-Withdraw]")
    private Integer typeDetail;

    @ApiModelProperty("Transfer message")
    private String typeMSG;

    @ApiModelProperty("Currency Type::[1=TRY 2=USD 3=EUR  Default=1")
    private Integer currencyType;

    @ApiModelProperty("Currency Message::[1=TRY 2=USD 3=EUR  Default=1")
    private String currency;

    @ApiModelProperty("Transfer Amount")
    private BigDecimal amount;

    @ApiModelProperty("Our Commission")
    private Double payCommission;

    @ApiModelProperty("Third company Commission")
    private Double channelCommission;

    @ApiModelProperty("Total Commission")
    private Double commission;

    @ApiModelProperty("Real Transfer Amount")
    private BigDecimal realAmount;

    @ApiModelProperty("Create Time")
    private String createTime;

    @ApiModelProperty("Complete Time")
    private String completeTime;

}
