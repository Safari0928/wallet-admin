package com.mdd.common.entity.payTransfer;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.hpsf.Decimal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("Transfer Details Entity")
public class PayTransferDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value="id", type= IdType.AUTO)
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

    @ApiModelProperty("Currency Type::[1=TRY 2=USD 3=EUR  Default=1")
    private Integer currencyType;

    @ApiModelProperty("Transfer Amount")
    private BigDecimal amount;

    @ApiModelProperty("Real Transfer Amount")
    private BigDecimal realAmount;

    @ApiModelProperty("Transfer Status:[-1:Fail 1:Success]]")
    private Integer status;

    @ApiModelProperty("Our Commission")
    private Double payCommission;

    @ApiModelProperty("Third company Commission")
    private Double channelCommission;

    @ApiModelProperty("Create Time")
    private String createTime;

    @ApiModelProperty("Complete Time")
    private String completeTime;

    @ApiModelProperty("Transfer Type:[3:Transfer-Deposit 4:Transfer-Withdraw]")
    private Integer typeDetail;

    @ApiModelProperty("Transfer UUID")
    private String transferId;
}
