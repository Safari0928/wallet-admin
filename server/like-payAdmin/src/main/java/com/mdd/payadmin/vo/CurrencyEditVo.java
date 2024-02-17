package com.mdd.payadmin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Admin Currency Edit Vo")
public class CurrencyEditVo {

    @ApiModelProperty("Currency Name")
    private String currencyName;

    @ApiModelProperty("Currency Code")
    private String currencyCode;

    @ApiModelProperty("Symbol")
    private String symbol;

    @ApiModelProperty("Operate")
    private Integer operate;

    @ApiModelProperty("Image")
    private String image;
}
