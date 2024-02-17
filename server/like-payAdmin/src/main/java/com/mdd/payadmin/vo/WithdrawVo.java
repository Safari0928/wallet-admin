package com.mdd.payadmin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("Admin Withdraw Vo")
public class WithdrawVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("Uuid")
    private String uuid;

    @ApiModelProperty("Transaction Number")
    private String transactionNumber;

    @ApiModelProperty("Iban")
    private String iban;

    @ApiModelProperty("Bank Name")
    private String bankName;

    @ApiModelProperty("Amount")
    private BigDecimal amount;

    @ApiModelProperty("Total Amount")
    private BigDecimal realAmount;

    @ApiModelProperty("Pay Id")
    private String payId;

    @ApiModelProperty("Transaction Status:[-1:Fail 1:Success 2:Pending]")
    private Integer status;

    @ApiModelProperty("Transaction Status Message")
    private String statusMessage;

    @ApiModelProperty("Create Time")
    private String createTime;

    @ApiModelProperty("Complete Time")
    private String completeTime;
}
