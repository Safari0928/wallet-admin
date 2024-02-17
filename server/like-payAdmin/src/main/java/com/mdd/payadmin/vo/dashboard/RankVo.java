package com.mdd.payadmin.vo.dashboard;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class RankVo {

    @ApiModelProperty("pay id")
    private String payId;

    @ApiModelProperty("country")
    private String country;

    @ApiModelProperty("amount")
    private BigDecimal amount;

    @ApiModelProperty("detail uuid")
    private String userId;
}
