package com.mdd.common.entity.payDeposit;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@ApiModel("Deposit")
public class PayDeposit implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("DepositID")
    private Integer id;

    @ApiModelProperty("Uuid")
    private String uuid;

    @ApiModelProperty("UserId")
    private String userId;

    @ApiModelProperty("Transact Number")
    private String transactionNumber;

    @ApiModelProperty("Bank Card Id")
    private String bankCardId;

    @ApiModelProperty("Amount")
    private BigDecimal amount;

    @ApiModelProperty("Real Amount")
    private BigDecimal realAmount;

    @ApiModelProperty("Commision")
    private Double payCommission ;

    @ApiModelProperty("Channel Commission")
    private Double channelCommission;

    @ApiModelProperty("PayId")
    private String payId;

    @ApiModelProperty("Type Detail")
    private Integer typeDetail;

    @ApiModelProperty("Deposit Status:[-1:Fail 1:Success]")
    private Integer status;

    @ApiModelProperty("创建时间,Create Time")
    private String  createTime;

    @ApiModelProperty("删除时间,Complete Time")
    private String  completeTime;
}
