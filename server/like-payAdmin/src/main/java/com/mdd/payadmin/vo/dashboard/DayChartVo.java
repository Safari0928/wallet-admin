package com.mdd.payadmin.vo.dashboard;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DayChartVo {
    @ApiModelProperty("day")
    private String day;

    @ApiModelProperty("Type Integer:1")
    private BigDecimal revenue;

    @ApiModelProperty("Type Integer:2")
    private BigDecimal deposit;

    @ApiModelProperty("Type Integer:3")
    private BigDecimal withdrawal;

    @ApiModelProperty("Type Integer:3")
    private BigDecimal transfer;

    @ApiModelProperty("Type Integer:4")
    private BigDecimal exchanges;

    @ApiModelProperty("Type Integer:5")
    private BigDecimal users;


}
