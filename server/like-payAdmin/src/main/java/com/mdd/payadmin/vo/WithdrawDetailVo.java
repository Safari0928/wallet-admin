package com.mdd.payadmin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("Admin Withdraw Detail Vo")
public class WithdrawDetailVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("Uuid")
    private String uuid;

    @ApiModelProperty("Aipay Transaction Number")
    private String aipayTransactionNumber;

    @ApiModelProperty("Bank Transaction Number")
    private String bankTransactionNumber;

    @ApiModelProperty("Pay Id")
    private String payId;

    @ApiModelProperty("Transaction Status:[-1:Fail 1:Success 2:Pending]")
    private Integer status;

    @ApiModelProperty("Transaction Status Message")
    private String statusMessage;

    @ApiModelProperty("Transfer Type:[3:Transfer-Deposit 4:Transfer-Withdraw]")
    private Integer typeDetail;

    @ApiModelProperty("Transfer message")
    private String typeMSG;

    @ApiModelProperty("Failed Description")
    private String description;

    @ApiModelProperty("Iban")
    private String iban;

    @ApiModelProperty("Bank Name")
    private String bankName;

    @ApiModelProperty("Phone Number Of Iban")
    private String phoneNumber;

    @ApiModelProperty("Amount")
    private BigDecimal amount;

    @ApiModelProperty("Our Commission")
    private Double payCommission;

    @ApiModelProperty("Third company Commission")
    private Double channelCommission;

    @ApiModelProperty("Total Commission")
    private Double commission;

    @ApiModelProperty("Total Amount")
    private BigDecimal realAmount;

    @ApiModelProperty("Create Time")
    private String createTime;

    @ApiModelProperty("Complete Time")
    private String completeTime;
}
