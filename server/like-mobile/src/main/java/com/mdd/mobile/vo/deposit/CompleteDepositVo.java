package com.mdd.mobile.vo.deposit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.math.BigDecimal;


@Data
@ApiModel(value = "Complete Deposit Vo")
public class CompleteDepositVo {

    @ApiModelProperty("Err message")
    private String  msg;

    @ApiModelProperty("Err Code")
    private Integer code;

    @ApiModelProperty("Data ")
    private BigDecimal amount;

}
