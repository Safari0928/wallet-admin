package com.mdd.payadmin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class TransferConfigureVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("Commission")
    private Double payCommission ;

    @ApiModelProperty("currencyId")
    private Integer currencyId;

    @ApiModelProperty("currencyId")
    private String currency;

    @ApiModelProperty("Unverified User Fee")
    private BigDecimal unverifiedUserFee;

    @ApiModelProperty("Verified User Fee ")
    private BigDecimal verifiedUserFee;

    @ApiModelProperty("Transfer configure uuid")
    private String uuid;
}
