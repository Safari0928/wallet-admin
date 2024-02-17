package com.mdd.mobile.vo.withdraw;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("Withdraw Vo")
public class WithdrawVo implements Serializable {

    @ApiModelProperty("Uuid")
    private String uuid;

    @ApiModelProperty("Iban")
    private String iban;

    @ApiModelProperty("Account Owner")
    private String nickName;

    @ApiModelProperty("Amount")
    private BigDecimal amount;

    @ApiModelProperty("Commission")
    private Double commission;

    @ApiModelProperty("Total Amount")
    private BigDecimal realAmount;

    @ApiModelProperty("Transfer Status:[-1:Fail 1:Success]")
    private Integer status;

}
