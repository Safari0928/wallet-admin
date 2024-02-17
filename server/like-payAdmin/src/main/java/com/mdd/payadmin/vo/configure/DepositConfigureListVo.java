package com.mdd.payadmin.vo.configure;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("Deposit Configure List Vo")
public class DepositConfigureListVo implements Serializable {

    @ApiModelProperty("uuid")
    private String uuid ;

    @ApiModelProperty("Commision")
    private Double payCommission ;

    @ApiModelProperty("Channel Commission")
    private Double channelCommission;

    @ApiModelProperty("Unverified User Fee")
    private BigDecimal unverifiedUserFee;

    @ApiModelProperty("Verified User Fee ")
    private BigDecimal verifiedUserFee;

}
