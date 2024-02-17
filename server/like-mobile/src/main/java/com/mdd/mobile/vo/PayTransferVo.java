package com.mdd.mobile.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "Transfer Vo")
public class PayTransferVo implements Serializable {

    @ApiModelProperty("Transaction Number")
    private String transactionNumber;

    @ApiModelProperty("Transfer Status:[-1:Fail 1:Success]")
    private Integer status;

    @ApiModelProperty("Transfer Status Message")
    private String statusMSG;
}
