package com.mdd.mobile.vo.withdraw;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("Verify Code Withdraw Vo")
public class VerifyWithdrawVo {

    @ApiModelProperty("Amount")
    private BigDecimal amount;

    @ApiModelProperty("Transfer Status:[-1:Fail 1:Success]")
    private String status;

    @ApiModelProperty("Currency Symbol (TRY, USD...)")
    private String currencySymbol;
}
