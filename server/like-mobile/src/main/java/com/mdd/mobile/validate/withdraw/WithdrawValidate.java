package com.mdd.mobile.validate.withdraw;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel("Withdraw Validate")
public class WithdrawValidate extends FullWithdrawValidate{

    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty("Amount")
    private BigDecimal amount;

}
