package com.mdd.payadmin.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
@Data
public class TransferDefaultVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("Transaction Number")
    private String transactionNumber;

    @ApiModelProperty("Currency Type::[1=TRY 2=USD 3=EUR  Default=1")
    private Integer currencyType;

    @ApiModelProperty("Currency Type::[1=TRY 2=USD 3=EUR  Default=1")
    private String currency;

    @ApiModelProperty("Transfer Amount")
    private BigDecimal amount;

    @ApiModelProperty("Real Transfer Amount")
    private BigDecimal realAmount;

    @ApiModelProperty("Pay ID")
    private String payId;

    @ApiModelProperty("Transfer Type:[3:Transfer-Deposit 4:Transfer-Withdraw]")
    private Integer typeDetail;

    @ApiModelProperty("Transfer message:[3:Transfer-Deposit 4:Transfer-Withdraw]")
    private String typeMSG;

    @ApiModelProperty("Transfer Status:[-1:Fail 1:Success]]")
    private Integer status;

    @ApiModelProperty("Transfer Status:[-1:Fail 1:Success]]")
    private String statusMSG;

    @ApiModelProperty("Create Time")
    private String createTime;

    @ApiModelProperty("Complete Time")
    private String completeTime;

    @ApiModelProperty("transfer ID")
    private String transferId;

}
