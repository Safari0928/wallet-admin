package com.mdd.common.entity.payCurrency;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("PayCurrency Entity")
public class PayCurrency {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("Id")
    private Integer id;

    @ApiModelProperty("uuid")
    private String  uuid;

    @ApiModelProperty("Currency Type")
    private Integer currencyType;

    @ApiModelProperty("isActiveForWithdraw")
    private Integer isActiveForWithdraw;

    @ApiModelProperty("isActiveForDeposit")
    private Integer isActiveForDeposit;

    @ApiModelProperty("isActiveForTransfer")
    private Integer isActiveForTransfer;

    @ApiModelProperty("Currency Name")
    private String currencyName;

    @ApiModelProperty("Currency Symbol")
    private String symbol;

    @ApiModelProperty("Currency Image")
    private String image;

    @ApiModelProperty("Currency Code")
    private String currencyCode;

    @ApiModelProperty("isActive")
    private Integer isActive;

}
