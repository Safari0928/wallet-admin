package com.mdd.mobile.vo.deposit;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "Err Vo")
public class ErrVo implements Serializable {

    @ApiModelProperty("Err message")
    private String  msg;

    @ApiModelProperty("Err Code")
    private Integer code;

    @ApiModelProperty("Data ")
    private T data;
}
