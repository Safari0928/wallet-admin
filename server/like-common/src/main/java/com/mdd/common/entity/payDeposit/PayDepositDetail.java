package com.mdd.common.entity.payDeposit;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("Deposit")
public class PayDepositDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("Deposit Detail ID")
    private Integer id;

    @ApiModelProperty("Uuid")
    private String uuid;

    @ApiModelProperty("UserId")
    private String userId;

    @ApiModelProperty("PayId")
    private String payId;

    @ApiModelProperty("Deposit Id")
    private String depositId;

    @ApiModelProperty("Transact Number")
    private String transactionNumber;

    @ApiModelProperty("Commision")
    private Integer typeDetail;

    @ApiModelProperty("Cart Number")
    private String cardNumber;

    @ApiModelProperty("Expiry date")
    private String expiryDate;

    @ApiModelProperty("CVV")
    private String cvv;

    @ApiModelProperty("Phone_number")
    private String phoneNumber;

    @ApiModelProperty("Transact Number")
    private String bankName;

    @ApiModelProperty("Transact Number")
    private String fullName;

    @ApiModelProperty("Cart Number")
    private BigDecimal amount;

    @ApiModelProperty("Transact Number")
    private BigDecimal realAmount;

    @ApiModelProperty("Commision")
    private Double commission ;

    @ApiModelProperty("Commision")
    private Double payCommission ;

    @ApiModelProperty("Channel Commission")
    private Double channelCommission;

    @ApiModelProperty(" status:[-1:Fail 1:Success]")
    private Integer status;

    @ApiModelProperty("创建时间,Create Time")
    private String createTime;

    @ApiModelProperty("更新时间,Update Time")
    private String completeTime;

}
