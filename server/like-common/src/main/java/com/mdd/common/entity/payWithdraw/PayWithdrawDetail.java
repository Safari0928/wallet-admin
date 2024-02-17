package com.mdd.common.entity.payWithdraw;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("PayWithdrawDetail Entity")
public class PayWithdrawDetail {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("Id")
    private Integer id;

    @ApiModelProperty("Uuid")
    private String uuid;

    @ApiModelProperty("User Id")
    private String userId;

    @ApiModelProperty("AiPay Id")
    private String payId;

    @ApiModelProperty("Withdraw Id")
    private String withdrawId;

    @ApiModelProperty("Transaction Number")
    private String transactionNumber;

    @ApiModelProperty("Iban")
    private String iban;

    @ApiModelProperty("Phone Number")
    private String phoneNumber;

    @ApiModelProperty("Bank Name")
    private String bankName;

    @ApiModelProperty("Amount")
    private BigDecimal amount;

    @ApiModelProperty("Real Amount")
    private BigDecimal realAmount;

    @ApiModelProperty("Commission")
    private Double commission;

    @ApiModelProperty("Status")
    private Integer status;

    @ApiModelProperty("Create Time")
    private String createTime;

    @ApiModelProperty("Complete Time")
    private String completeTime;

    @ApiModelProperty("Failed description")
    private String description;

}
