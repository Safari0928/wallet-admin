package com.mdd.mobile.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "Transfer Detail Vo")
public class PayTransferDetailVo implements Serializable {

    @ApiModelProperty("User NickName")
    private String nickName;

    @ApiModelProperty("Receiver NickName")
    private String toNickName;

    @ApiModelProperty("Transaction Number")
    private String transactionNumber;

    @ApiModelProperty("Transfer Amount")
    private BigDecimal amount;

    @ApiModelProperty("Commission")
    private Double commission;

    @ApiModelProperty("Transfer Status:[-1:Fail 1:Success]")
    private Integer status;

    @ApiModelProperty("Transfer Status Massage")
    private String statusMSG;

    @ApiModelProperty("Transfer Type:[3:Transfer-Deposit 4:Transfer-Withdraw]")
    private Integer typeDetail;

    @ApiModelProperty("Transfer Type Message")
    private String typeMSG;

    @ApiModelProperty("Create Time")
    private String createTime;

    @ApiModelProperty("Complete Time")
    private String completeTime;
}
