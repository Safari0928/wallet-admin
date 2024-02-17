package com.mdd.mobile.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "Transfer Details Vo")
public class PayTransferDetailsVo implements Serializable {

    @ApiModelProperty("Receiver NickName")
    private String toNickName;

    @ApiModelProperty("Transfer Amount")
    private BigDecimal amount;

    @ApiModelProperty("Transfer Type:[3:Transfer-Deposit 4:Transfer-Withdraw]")
    private Integer typeDetail;

    @ApiModelProperty("Transfer Type Message")
    private String typeMSG;

    @ApiModelProperty("Create Time")
    private String createTime;

    @ApiModelProperty("Complete Time")
    private String completeTime;

    @ApiModelProperty("Transfer UUID")
    private String transferId;
}
