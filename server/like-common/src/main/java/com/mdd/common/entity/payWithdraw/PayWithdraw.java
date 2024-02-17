package com.mdd.common.entity.payWithdraw;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("PayWithdraw Entity")
public class PayWithdraw {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("Id")
    private Integer id;

    @ApiModelProperty("Uuid")
    private String uuid;

    @ApiModelProperty("AiPay Id")
    private String payId;

    @ApiModelProperty("Transaction Number")
    private String transactionNumber;

    @ApiModelProperty("Iban")
    private String iban;

    @ApiModelProperty("Currency Type (TRY, USD...)")
    private Integer currencyType;

    @ApiModelProperty("Bank Name")
    private String bankName;

    @ApiModelProperty("Amount")
    private BigDecimal amount;

    @ApiModelProperty("Real Amount")
    private BigDecimal realAmount;

    @ApiModelProperty("User Id")
    private String userId;

    @ApiModelProperty("Status")
    private Integer status;

    @ApiModelProperty("Create Time")
    private String createTime;

    @ApiModelProperty("Complete Time")
    private String completeTime;

    @ApiModelProperty("Commission")
    private Double commission;

    @ApiModelProperty("Pay Commission")
    private Double payCommission;

    @ApiModelProperty("Chanel Commission")
    private Double channelCommission;

    @ApiModelProperty("Type Detail")
    private Integer typeDetail;

}
