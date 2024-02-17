package com.mdd.mobile.vo.withdraw;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("Withdraw Detail Vo")
public class WithdrawDetailsVo {

    @ApiModelProperty("Bank Name")
    private String bankName;

    @ApiModelProperty("The last 4 digits of the Iban")
    private String iban;

    @ApiModelProperty("Amount")
    private BigDecimal amount;

    @ApiModelProperty("Create Time")
    private String createTime;

}
