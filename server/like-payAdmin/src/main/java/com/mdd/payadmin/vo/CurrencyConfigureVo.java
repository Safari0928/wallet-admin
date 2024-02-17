package com.mdd.payadmin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Admin Currency Configure Default Page")
public class CurrencyConfigureVo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("Currency Id")
    private String currencyId;

    @ApiModelProperty("Currency Name")
    private String currencyName;

    @ApiModelProperty("Currency Code")
    private String currencyCode;

    @ApiModelProperty("Operate")
    private Integer operate;

    @ApiModelProperty("Symbol")
    private String symbol;

}
