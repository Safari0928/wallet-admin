package com.mdd.common.entity.payLimits;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("Transfer limits")
public class PayTransferConfigure implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("Transfer Limits ID")
    private Integer id;

    @ApiModelProperty("Transfer limits uuid")
    private String uuid;

    @ApiModelProperty("Unverified User Fee")
    private BigDecimal unverifiedUserFee;

    @ApiModelProperty("Verified User Fee ")
    private BigDecimal verifiedUserFee;

    @ApiModelProperty(" Is Active Limit :1 active -1:un active")
    private Integer isActive;

    @ApiModelProperty("Create Time")
    private String  createTime;

    @ApiModelProperty("Delete Time")
    private String  updateTime;

    @ApiModelProperty("Commission")
    private Double commission;

    @ApiModelProperty("Commission")
    private Double payCommission ;

    @ApiModelProperty("Channel Commission")
    private Double channelCommission;

    @ApiModelProperty("currencyId")
    private Integer currencyId;

}
