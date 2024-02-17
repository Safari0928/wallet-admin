package com.mdd.common.entity.payTransfer;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("Transfer Entity")
public class PayTransfer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value="id", type= IdType.AUTO)
    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("uuid")
    private String uuid;

    @ApiModelProperty("User ID")
    private String userId;

    @ApiModelProperty("Receiver ID")
    private String toUserId;

    @ApiModelProperty("User NickName")
    private String nickName;

    @ApiModelProperty("Receiver NickName")
    private String toNickName;

    @ApiModelProperty("Pay ID")
    private String payId;

    @ApiModelProperty("Receiver pay ID")
    private String toPayId;

    @ApiModelProperty("Transaction Number")
    private String transactionNumber;

    @ApiModelProperty("Currency Type:[1=TRY 2=USD 3=EUR  Default=1]")
    private Integer currencyType;

    @ApiModelProperty("Transfer Amount")
    private BigDecimal amount;

    @ApiModelProperty("Transfer Status:[0:Fail 1:Success,default:0]")
    private Integer status;

    @ApiModelProperty("Our Commission")
    private Double payCommission;

    @ApiModelProperty("Third company Commission")
    private Double channelCommission;

    @ApiModelProperty("Transfer Type:[3:Transfer-Deposit 4:Transfer-Withdraw]")
    private Integer typeDetail;

}
