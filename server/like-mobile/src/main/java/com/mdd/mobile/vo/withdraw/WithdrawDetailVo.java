package com.mdd.mobile.vo.withdraw;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("Withdraw Detail Vo")
public class WithdrawDetailVo {

    @ApiModelProperty("Transaction Status (Succesful/Fail)")
    private String status;

    @ApiModelProperty("Amount")
    private BigDecimal amount;

    @ApiModelProperty("Create Time")
    private String createTime;

    @ApiModelProperty("Complete Time")
    private String completeTime;

    @ApiModelProperty("Transaction Type")
    private String transactionType;

    @ApiModelProperty("Bank Name")
    private String bankName;

    @ApiModelProperty("Commission")
    private Double commission;

    @ApiModelProperty("Transaction Number")
    private String transactionNumber;

}
