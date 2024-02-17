package com.mdd.payadmin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("Admin Withdraw Configure Default Page")
public class WithdrawConfigureVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("Withdraw configure uuid")
    private String uuid;

    @ApiModelProperty("Commission")
    private Double payCommission ;

    @ApiModelProperty("Unverified User Fee")
    private BigDecimal unverifiedUserFee;

    @ApiModelProperty("Verified User Fee ")
    private BigDecimal verifiedUserFee;

    @ApiModelProperty("Amount Audited")
    private BigDecimal amountAudited;

    @ApiModelProperty("Currency Id")
    private Integer currencyId;

    @ApiModelProperty("Currency Name")
    private String currency;

}
