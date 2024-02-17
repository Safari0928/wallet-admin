package com.mdd.payadmin.vo.dashboard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("Dashboard Panel Vo")
public class PanelVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("Total Revenue")
    private BigDecimal totalRevenue;

    @ApiModelProperty("Total Trading Volume")
    private BigDecimal totalTradingVolume;

    @ApiModelProperty("Registered user")
    private Integer registeredUser;

    @ApiModelProperty("Total Orders")
    private Integer totalOrders;

    @ApiModelProperty("Total Withdrawal Volume")
    private BigDecimal totalWithdrawalVolume;

    @ApiModelProperty("Total Deposit Volume")
    private BigDecimal totalDepositVolume;

    @ApiModelProperty("Total Transfer Volume")
    private BigDecimal totalTransferVolume;

    @ApiModelProperty("Total Exchanges Volume")
    private BigDecimal totalExchangesVolume;

}
