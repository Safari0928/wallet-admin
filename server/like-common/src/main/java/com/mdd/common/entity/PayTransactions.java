package com.mdd.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;

@Data
@ApiModel("Transactions Entity")
public class PayTransactions {

    private static final long serialVersionUID = 1L;

    @TableId(value="id", type= IdType.AUTO)
    private Integer id;

    @ApiModelProperty("uuid")
    private String uuid;

    @ApiModelProperty("User ID")
    private String userId;

    @ApiModelProperty("Pay ID")
    private String payId;

    @ApiModelProperty("Transaction Number")
    private String transactionNumber;

    @ApiModelProperty("Detail ID")
    private String detailId;

    @ApiModelProperty("Currency Type::[1=TRY 2=USD 3=EUR  Default=1")
    private Integer currencyType;

    @ApiModelProperty("Transfer Amount")
    private BigDecimal amount;

    @ApiModelProperty("Real Transfer Amount")
    private BigDecimal realAmount;

    @ApiModelProperty("Our Commission")
    private Double payCommission;

    @ApiModelProperty("Third company Commission")
    private Double channelCommission;

    @ApiModelProperty("revenue")
    private BigDecimal revenue;

    @ApiModelProperty("Transfer Type:[1:Deposit 2:Withdraw 3:Transfer-Deposit 4:Transfer-Withdraw]")
    private Integer typeDetail;

    @ApiModelProperty("Transfer Status:[-1:Fail 1:Success]]")
    private Integer status;

    @ApiModelProperty("Details : Last 4 digit, Nickname")
    private String detail;

    @ApiModelProperty("Create Time")
    private String createTime;

    @ApiModelProperty("Complete Time")
    private String completeTime;
}
